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

        if (!allSensorsRegistered(payload, hubId)) {
            return;
        }

        Scenario scenario = getOrCreateScenario(hubId, payload.getName());
        updateConditions(scenario, payload);
        updateActions(scenario, payload);

        scenarioRepository.save(scenario);
        log.info("Сценарий '{}' сохранён для хаба {}", payload.getName(), hubId);
    }

    private boolean allSensorsRegistered(ScenarioAddedEventAvro payload, String hubId) {
        Set<String> sensorIds = new HashSet<>();
        payload.getConditions().forEach(condition -> sensorIds.add(condition.getSensorId()));
        payload.getActions().forEach(action -> sensorIds.add(action.getSensorId()));

        for (String sensorId : sensorIds) {
            if (sensorRepository.findByIdAndHubId(sensorId, hubId).isEmpty()) {
                log.warn("Сценарий '{}' пропущен: датчик {} не зарегистрирован в хабе {}",
                        payload.getName(), sensorId, hubId);
                return false;
            }
        }
        return true;
    }

    private Scenario getOrCreateScenario(String hubId, String name) {
        return scenarioRepository.findByHubIdAndName(hubId, name)
                .orElseGet(() -> Scenario.builder()
                        .hubId(hubId)
                        .name(name)
                        .build());
    }

    private void updateConditions(Scenario scenario, ScenarioAddedEventAvro payload) {
        scenario.getConditions().clear();
        for (ScenarioConditionAvro condition : payload.getConditions()) {
            scenario.getConditions().put(condition.getSensorId(), toCondition(condition));
        }
    }

    private void updateActions(Scenario scenario, ScenarioAddedEventAvro payload) {
        scenario.getActions().clear();
        for (DeviceActionAvro action : payload.getActions()) {
            scenario.getActions().put(action.getSensorId(), toAction(action));
        }
    }

    private Condition toCondition(ScenarioConditionAvro conditionAvro) {
        return Condition.builder()
                .type(ConditionType.valueOf(conditionAvro.getType().name()))
                .operation(ConditionOperation.valueOf(conditionAvro.getOperation().name()))
                .value(toConditionValue(conditionAvro))
                .build();
    }

    private Action toAction(DeviceActionAvro actionAvro) {
        return Action.builder()
                .type(ActionType.valueOf(actionAvro.getType().name()))
                .value(actionAvro.getValue())
                .build();
    }

    private Integer toConditionValue(ScenarioConditionAvro conditionAvro) {
        return switch (conditionAvro.getValue()) {
            case Integer intValue -> intValue;
            case Boolean boolValue -> boolValue ? 1 : 0;
            case null, default -> null;
        };
    }
}
