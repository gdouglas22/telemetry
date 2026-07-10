package ru.yandex.practicum.collector.handler.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;

@Component
public class DeviceRemovedEventHandler implements HubEventHandler {

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }

    @Override
    public SpecificRecordBase mapToAvro(HubEventProto event) {
        return DeviceRemovedEventAvro.newBuilder()
                .setId(event.getDeviceRemoved().getId())
                .build();
    }
}
