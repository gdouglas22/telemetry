package ru.yandex.practicum.collector.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.model.sensor.ClimateSensorEvent;
import ru.yandex.practicum.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Component
public class ClimateSensorEventHandler implements SensorEventHandler {

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }

    @Override
    public SpecificRecordBase mapToAvro(SensorEvent event) {
        ClimateSensorEvent e = (ClimateSensorEvent) event;
        return ClimateSensorAvro.newBuilder()
                .setTemperatureC(e.getTemperatureC())
                .setHumidity(e.getHumidity())
                .setCo2Level(e.getCo2Level())
                .build();
    }
}
