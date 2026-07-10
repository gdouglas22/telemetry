package ru.yandex.practicum.analyzer.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.analyzer.model.Scenario;
import ru.yandex.practicum.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.analyzer.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceRemovedEventHandler implements HubEventHandler {

    private final SensorRepository sensorRepository;
    private final ScenarioRepository scenarioRepository;

    @Override
    public Class<?> getPayloadType() {
        return DeviceRemovedEventAvro.class;
    }

    @Override
    @Transactional
    public void handle(HubEventAvro event) {
        DeviceRemovedEventAvro payload = (DeviceRemovedEventAvro) event.getPayload();
        sensorRepository.findByIdAndHubId(payload.getId(), event.getHubId()).ifPresent(sensor -> {
            // сначала убираем ссылки на датчик из сценариев хаба, чтобы не нарушить целостность
            List<Scenario> scenarios = scenarioRepository.findByHubId(event.getHubId());
            for (Scenario scenario : scenarios) {
                scenario.getConditions().remove(sensor.getId());
                scenario.getActions().remove(sensor.getId());
            }
            scenarioRepository.saveAll(scenarios);
            sensorRepository.delete(sensor);
            log.info("Датчик {} удалён из хаба {}", sensor.getId(), event.getHubId());
        });
    }
}
