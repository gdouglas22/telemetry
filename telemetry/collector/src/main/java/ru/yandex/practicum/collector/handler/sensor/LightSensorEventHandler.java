package ru.yandex.practicum.collector.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component
public class LightSensorEventHandler implements SensorEventHandler {

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR_EVENT;
    }

    @Override
    public SpecificRecordBase mapToAvro(SensorEventProto event) {
        LightSensorProto sensor = event.getLightSensorEvent();
        return LightSensorAvro.newBuilder()
                .setLinkQuality(sensor.getLinkQuality())
                .setLuminosity(sensor.getLuminosity())
                .build();
    }
}
