package ru.yandex.practicum.collector.service;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.collector.handler.hub.HubEventHandler;
import ru.yandex.practicum.collector.handler.sensor.SensorEventHandler;
import ru.yandex.practicum.collector.model.hub.HubEvent;
import ru.yandex.practicum.collector.model.hub.HubEventType;
import ru.yandex.practicum.collector.model.sensor.SensorEvent;
import ru.yandex.practicum.collector.model.sensor.SensorEventType;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EventCollectorServiceImpl implements EventCollectorService {

    private final KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;
    private final String sensorsTopic;
    private final String hubsTopic;
    private final Map<SensorEventType, SensorEventHandler> sensorHandlers;
    private final Map<HubEventType, HubEventHandler> hubHandlers;

    public EventCollectorServiceImpl(
            KafkaTemplate<String, SpecificRecordBase> kafkaTemplate,
            List<SensorEventHandler> sensorHandlers,
            List<HubEventHandler> hubHandlers,
            @Value("${collector.kafka.topics.sensors}") String sensorsTopic,
            @Value("${collector.kafka.topics.hubs}") String hubsTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.sensorsTopic = sensorsTopic;
        this.hubsTopic = hubsTopic;
        this.sensorHandlers = sensorHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageType, Function.identity()));
        this.hubHandlers = hubHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageType, Function.identity()));
    }

    @Override
    public void collectSensorEvent(SensorEvent event) {
        SensorEventHandler handler = sensorHandlers.get(event.getType());
        if (handler == null) {
            throw new IllegalArgumentException("Unknown sensor event type: " + event.getType());
        }
        SensorEventAvro avro = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(handler.mapToAvro(event))
                .build();
        kafkaTemplate.send(sensorsTopic, event.getHubId(), avro);
    }

    @Override
    public void collectHubEvent(HubEvent event) {
        HubEventHandler handler = hubHandlers.get(event.getType());
        if (handler == null) {
            throw new IllegalArgumentException("Unknown hub event type: " + event.getType());
        }
        HubEventAvro avro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(handler.mapToAvro(event))
                .build();
        kafkaTemplate.send(hubsTopic, event.getHubId(), avro);
    }
}
