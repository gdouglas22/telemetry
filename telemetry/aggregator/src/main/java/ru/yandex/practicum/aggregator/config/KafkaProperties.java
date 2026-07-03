package ru.yandex.practicum.aggregator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "aggregator.kafka")
public class KafkaProperties {

    private String bootstrapServers;
    private String groupId;
    private String autoOffsetReset;
    private long pollTimeoutMs;
    private Topics topics = new Topics();

    @Getter
    @Setter
    public static class Topics {
        private String sensors;
        private String snapshots;
    }
}
