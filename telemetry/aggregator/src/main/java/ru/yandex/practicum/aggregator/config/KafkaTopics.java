package ru.yandex.practicum.aggregator.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaTopics {

    private String sensors;
    private String snapshots;
}
