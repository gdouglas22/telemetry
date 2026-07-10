package ru.yandex.practicum.analyzer.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumerSettings {

    private String groupId;
    private String autoOffsetReset;
}
