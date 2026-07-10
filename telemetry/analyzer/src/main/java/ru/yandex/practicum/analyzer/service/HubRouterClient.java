package ru.yandex.practicum.analyzer.service;

import ru.yandex.practicum.analyzer.model.Action;
import ru.yandex.practicum.analyzer.model.Scenario;

public interface HubRouterClient {

    void sendAction(Scenario scenario, String sensorId, Action action);
}
