package ru.yandex.practicum.analyzer.handler;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubEventHandler {

    Class<?> getPayloadType();

    void handle(HubEventAvro event);
}
