package ru.yandex.practicum.collector.handler.sensor;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.collector.model.sensor.SensorEventType;

public interface SensorEventHandler {

    SensorEventType getMessageType();

    SpecificRecordBase mapToAvro(SensorEvent event);
}
