package ru.yandex.practicum.collector.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.model.sensor.MotionSensorEvent;
import ru.yandex.practicum.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

@Component
public class MotionSensorEventHandler implements SensorEventHandler {

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }

    @Override
    public SpecificRecordBase mapToAvro(SensorEvent event) {
        MotionSensorEvent e = (MotionSensorEvent) event;
        return MotionSensorAvro.newBuilder()
                .setLinkQuality(e.getLinkQuality())
                .setMotion(e.isMotion())
                .setVoltage(e.getVoltage())
                .build();
    }
}
