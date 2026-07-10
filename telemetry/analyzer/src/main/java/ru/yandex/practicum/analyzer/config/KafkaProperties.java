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
    private KafkaTopics topics = new KafkaTopics();
    private ConsumerSettings hubConsumer = new ConsumerSettings();
    private ConsumerSettings snapshotConsumer = new ConsumerSettings();
}
