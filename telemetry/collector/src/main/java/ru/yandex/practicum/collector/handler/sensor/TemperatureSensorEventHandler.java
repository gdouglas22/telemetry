package ru.yandex.practicum.collector.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.collector.model.sensor.TemperatureSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
public class TemperatureSensorEventHandler implements SensorEventHandler {

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    public SpecificRecordBase mapToAvro(SensorEvent event) {
        TemperatureSensorEvent e = (TemperatureSensorEvent) event;
        return TemperatureSensorAvro.newBuilder()
                .setId(e.getId())
                .setHubId(e.getHubId())
                .setTimestamp(e.getTimestamp())
                .setTemperatureC(e.getTemperatureC())
                .setTemperatureF(e.getTemperatureF())
                .build();
    }
}
