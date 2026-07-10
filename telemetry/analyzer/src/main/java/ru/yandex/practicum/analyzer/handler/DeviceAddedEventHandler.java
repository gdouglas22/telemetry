package ru.yandex.practicum.analyzer.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.analyzer.model.Sensor;
import ru.yandex.practicum.analyzer.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceAddedEventHandler implements HubEventHandler {

    private final SensorRepository sensorRepository;

    @Override
    public Class<?> getPayloadType() {
        return DeviceAddedEventAvro.class;
    }

    @Override
    @Transactional
    public void handle(HubEventAvro event) {
        DeviceAddedEventAvro payload = (DeviceAddedEventAvro) event.getPayload();
        if (sensorRepository.existsById(payload.getId())) {
            log.debug("Датчик {} уже зарегистрирован, событие пропущено", payload.getId());
            return;
        }
        sensorRepository.save(Sensor.builder()
                .id(payload.getId())
                .hubId(event.getHubId())
                .build());
        log.info("Датчик {} добавлен в хаб {}", payload.getId(), event.getHubId());
    }
}
