package ru.yandex.practicum.collector.handler.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.model.hub.DeviceRemovedEvent;
import ru.yandex.practicum.collector.model.hub.HubEvent;
import ru.yandex.practicum.collector.model.hub.HubEventType;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;

@Component
public class DeviceRemovedEventHandler implements HubEventHandler {

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_REMOVED;
    }

    @Override
    public SpecificRecordBase mapToAvro(HubEvent event) {
        DeviceRemovedEvent e = (DeviceRemovedEvent) event;
        return DeviceRemovedEventAvro.newBuilder()
                .setId(e.getId())
                .build();
    }
}
