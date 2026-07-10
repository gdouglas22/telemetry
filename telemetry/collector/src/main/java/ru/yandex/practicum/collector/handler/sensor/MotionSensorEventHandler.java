package ru.yandex.practicum.collector.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

@Component
public class MotionSensorEventHandler implements SensorEventHandler {

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_EVENT;
    }

    @Override
    public SpecificRecordBase mapToAvro(SensorEventProto event) {
        MotionSensorProto sensor = event.getMotionSensorEvent();
        return MotionSensorAvro.newBuilder()
                .setLinkQuality(sensor.getLinkQuality())
                .setMotion(sensor.getMotion())
                .setVoltage(sensor.getVoltage())
                .build();
    }
}
