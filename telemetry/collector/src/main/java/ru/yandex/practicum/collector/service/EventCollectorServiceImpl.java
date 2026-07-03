package ru.yandex.practicum.collector.service;

import com.google.protobuf.Timestamp;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.collector.handler.hub.HubEventHandler;
import ru.yandex.practicum.collector.handler.sensor.SensorEventHandler;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EventCollectorServiceImpl implements EventCollectorService {

    private final KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;
    private final String sensorsTopic;
    private final String hubsTopic;
    private final Map<SensorEventProto.PayloadCase, SensorEventHandler> sensorHandlers;
    private final Map<HubEventProto.PayloadCase, HubEventHandler> hubHandlers;

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
    public void collectSensorEvent(SensorEventProto event) {
        SensorEventHandler handler = sensorHandlers.get(event.getPayloadCase());
        if (handler == null) {
            throw new IllegalArgumentException("Unknown sensor event payload: " + event.getPayloadCase());
        }
        SensorEventAvro avro = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(mapTimestamp(event.getTimestamp()))
                .setPayload(handler.mapToAvro(event))
                .build();
        kafkaTemplate.send(sensorsTopic, event.getHubId(), avro);
    }

    @Override
    public void collectHubEvent(HubEventProto event) {
        HubEventHandler handler = hubHandlers.get(event.getPayloadCase());
        if (handler == null) {
            throw new IllegalArgumentException("Unknown hub event payload: " + event.getPayloadCase());
        }
        HubEventAvro avro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(mapTimestamp(event.getTimestamp()))
                .setPayload(handler.mapToAvro(event))
                .build();
        kafkaTemplate.send(hubsTopic, event.getHubId(), avro);
    }

    private Instant mapTimestamp(Timestamp timestamp) {
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }
}
