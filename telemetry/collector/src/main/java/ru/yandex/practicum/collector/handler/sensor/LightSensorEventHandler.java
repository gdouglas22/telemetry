package ru.yandex.practicum.collector.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.model.sensor.LightSensorEvent;
import ru.yandex.practicum.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component
public class LightSensorEventHandler implements SensorEventHandler {

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }

    @Override
    public SpecificRecordBase mapToAvro(SensorEvent event) {
        LightSensorEvent e = (LightSensorEvent) event;
        return LightSensorAvro.newBuilder()
                .setLinkQuality(e.getLinkQuality())
                .setLuminosity(e.getLuminosity())
                .build();
    }
}
