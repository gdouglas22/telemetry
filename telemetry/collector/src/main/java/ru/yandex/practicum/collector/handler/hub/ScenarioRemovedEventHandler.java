package ru.yandex.practicum.collector.handler.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.model.hub.HubEvent;
import ru.yandex.practicum.collector.model.hub.HubEventType;
import ru.yandex.practicum.collector.model.hub.ScenarioRemovedEvent;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
public class ScenarioRemovedEventHandler implements HubEventHandler {

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_REMOVED;
    }

    @Override
    public SpecificRecordBase mapToAvro(HubEvent event) {
        ScenarioRemovedEvent e = (ScenarioRemovedEvent) event;
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(e.getName())
                .build();
    }
}
