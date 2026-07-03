package ru.yandex.practicum.analyzer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "analyzer.kafka")
public class KafkaProperties {

    private String bootstrapServers;
    private long pollTimeoutMs;
    private Topics topics = new Topics();
    private Consumer hubConsumer = new Consumer();
    private Consumer snapshotConsumer = new Consumer();

    @Getter
    @Setter
    public static class Topics {
        private String hubs;
        private String snapshots;
    }

    @Getter
    @Setter
    public static class Consumer {
        private String groupId;
        private String autoOffsetReset;
    }
}
