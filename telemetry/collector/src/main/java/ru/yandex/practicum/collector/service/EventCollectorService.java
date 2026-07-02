package ru.yandex.practicum.collector.service;

import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface EventCollectorService {
    void collectSensorEvent(SensorEventProto event);

    void collectHubEvent(HubEventProto event);
}
