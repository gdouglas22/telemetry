package ru.yandex.practicum.analyzer.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.analyzer.model.Action;
import ru.yandex.practicum.analyzer.model.ActionType;
import ru.yandex.practicum.analyzer.model.Condition;
import ru.yandex.practicum.analyzer.model.ConditionOperation;
import ru.yandex.practicum.analyzer.model.ConditionType;
import ru.yandex.practicum.analyzer.model.Scenario;
import ru.yandex.practicum.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.analyzer.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScenarioAddedEventHandler implements HubEventHandler {

    private final ScenarioRepository scenarioRepository;
    private final SensorRepository sensorRepository;

    @Override
    public Class<?> getPayloadType() {
        return ScenarioAddedEventAvro.class;
    }

    @Override
    @Transactional
    public void handle(HubEventAvro event) {
        ScenarioAddedEventAvro payload = (ScenarioAddedEventAvro) event.getPayload();
        String hubId = event.getHubId();

        Set<String> sensorIds = new HashSet<>();
        payload.getConditions().forEach(condition -> sensorIds.add(condition.getSensorId()));
        payload.getActions().forEach(action -> sensorIds.add(action.getSensorId()));
        for (String sensorId : sensorIds) {
            if (sensorRepository.findByIdAndHubId(sensorId, hubId).isEmpty()) {
                log.warn("Сценарий '{}' пропущен: датчик {} не зарегистрирован в хабе {}",
                        payload.getName(), sensorId, hubId);
                return;
            }
        }

        Scenario scenario = scenarioRepository.findByHubIdAndName(hubId, payload.getName())
                .orElseGet(() -> Scenario.builder()
                        .hubId(hubId)
                        .name(payload.getName())
                        .build());

        scenario.getConditions().clear();
        for (ScenarioConditionAvro condition : payload.getConditions()) {
            scenario.getConditions().put(condition.getSensorId(), Condition.builder()
                    .type(ConditionType.valueOf(condition.getType().name()))
                    .operation(ConditionOperation.valueOf(condition.getOperation().name()))
                    .value(mapConditionValue(condition.getValue()))
                    .build());
        }

        scenario.getActions().clear();
        for (DeviceActionAvro action : payload.getActions()) {
            scenario.getActions().put(action.getSensorId(), Action.builder()
                    .type(ActionType.valueOf(action.getType().name()))
                    .value(action.getValue())
                    .build());
        }

        scenarioRepository.save(scenario);
        log.info("Сценарий '{}' сохранён для хаба {}", payload.getName(), hubId);
    }

    private Integer mapConditionValue(Object value) {
        if (value instanceof Integer intValue) {
            return intValue;
        }
        if (value instanceof Boolean boolValue) {
            return boolValue ? 1 : 0;
        }
        return null;
    }
}
