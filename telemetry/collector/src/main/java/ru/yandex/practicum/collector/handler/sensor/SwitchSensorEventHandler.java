package ru.yandex.practicum.collector.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.collector.model.sensor.SwitchSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
public class SwitchSensorEventHandler implements SensorEventHandler {

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }

    @Override
    public SpecificRecordBase mapToAvro(SensorEvent event) {
        SwitchSensorEvent e = (SwitchSensorEvent) event;
        return SwitchSensorAvro.newBuilder()
                .setState(e.isState())
                .build();
    }
}
