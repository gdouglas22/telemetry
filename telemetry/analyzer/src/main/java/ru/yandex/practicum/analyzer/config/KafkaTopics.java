package ru.yandex.practicum.analyzer.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaTopics {

    private String hubs;
    private String snapshots;
}
