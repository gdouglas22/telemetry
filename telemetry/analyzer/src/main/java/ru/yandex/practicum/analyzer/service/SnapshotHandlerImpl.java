package ru.yandex.practicum.analyzer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.analyzer.model.Condition;
import ru.yandex.practicum.analyzer.model.ConditionType;
import ru.yandex.practicum.analyzer.model.Scenario;
import ru.yandex.practicum.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnapshotHandlerImpl implements SnapshotHandler {

    private final ScenarioRepository scenarioRepository;
    private final HubRouterClient hubRouterClient;

    @Override
    public void handle(SensorsSnapshotAvro snapshot) {
        List<Scenario> scenarios = scenarioRepository.findByHubId(snapshot.getHubId());
        scenarios.stream()
                .filter(scenario -> matchesConditions(scenario, snapshot))
                .forEach(this::executeActions);
    }

    private boolean matchesConditions(Scenario scenario, SensorsSnapshotAvro snapshot) {
        return !scenario.getConditions().isEmpty() && scenario.getConditions().entrySet().stream()
                .allMatch(entry -> checkCondition(entry.getKey(), entry.getValue(), snapshot));
    }

    private boolean checkCondition(String sensorId, Condition condition, SensorsSnapshotAvro snapshot) {
        SensorStateAvro state = snapshot.getSensorsState().get(sensorId);
        if (state == null || condition.getValue() == null) {
            return false;
        }
        Integer current = extractValue(condition.getType(), state.getData());
        if (current == null) {
            return false;
        }
        return switch (condition.getOperation()) {
            case EQUALS -> current.equals(condition.getValue());
            case GREATER_THAN -> current > condition.getValue();
            case LOWER_THAN -> current < condition.getValue();
        };
    }

    private Integer extractValue(ConditionType type, Object data) {
        return switch (type) {
            case MOTION -> data instanceof MotionSensorAvro motion
                    ? (motion.getMotion() ? 1 : 0) : null;
            case LUMINOSITY -> data instanceof LightSensorAvro light
                    ? light.getLuminosity() : null;
            case SWITCH -> data instanceof SwitchSensorAvro switchSensor
                    ? (switchSensor.getState() ? 1 : 0) : null;
            case TEMPERATURE -> extractTemperature(data);
            case CO2LEVEL -> data instanceof ClimateSensorAvro climate
                    ? climate.getCo2Level() : null;
            case HUMIDITY -> data instanceof ClimateSensorAvro climate
                    ? climate.getHumidity() : null;
        };
    }

    private Integer extractTemperature(Object data) {
        if (data instanceof ClimateSensorAvro climate) {
            return climate.getTemperatureC();
        }
        if (data instanceof TemperatureSensorAvro temperature) {
            return temperature.getTemperatureC();
        }
        return null;
    }

    private void executeActions(Scenario scenario) {
        log.info("Сценарий '{}' хаба {} активирован", scenario.getName(), scenario.getHubId());
        scenario.getActions().forEach((sensorId, action) ->
                hubRouterClient.sendAction(scenario, sensorId, action));
    }
}
