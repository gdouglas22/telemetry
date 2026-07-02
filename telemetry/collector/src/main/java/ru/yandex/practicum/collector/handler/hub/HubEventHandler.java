package ru.yandex.practicum.collector.handler.hub;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.collector.model.hub.HubEvent;
import ru.yandex.practicum.collector.model.hub.HubEventType;

public interface HubEventHandler {

    HubEventType getMessageType();

    SpecificRecordBase mapToAvro(HubEvent event);
}
