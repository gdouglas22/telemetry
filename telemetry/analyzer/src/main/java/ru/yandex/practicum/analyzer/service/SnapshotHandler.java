package ru.yandex.practicum.analyzer.service;

import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public interface SnapshotHandler {

    void handle(SensorsSnapshotAvro snapshot);
}
