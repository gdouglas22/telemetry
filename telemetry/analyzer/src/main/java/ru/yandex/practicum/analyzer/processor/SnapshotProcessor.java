package ru.yandex.practicum.analyzer.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.config.KafkaProperties;
import ru.yandex.practicum.analyzer.service.SnapshotHandler;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotProcessor {

    private final KafkaConsumer<String, SensorsSnapshotAvro> snapshotConsumer;
    private final SnapshotHandler snapshotHandler;
    private final KafkaProperties properties;

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(snapshotConsumer::wakeup));
        try {
            snapshotConsumer.subscribe(List.of(properties.getTopics().getSnapshots()));

            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records =
                        snapshotConsumer.poll(Duration.ofMillis(properties.getPollTimeoutMs()));
                for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                    snapshotHandler.handle(record.value());
                }
                if (!records.isEmpty()) {
                    snapshotConsumer.commitSync();
                }
            }
        } catch (WakeupException ignored) {
            // игнорируем — закрываем консьюмер в блоке finally
        } catch (Exception e) {
            log.error("Ошибка во время обработки снапшотов", e);
        } finally {
            try {
                snapshotConsumer.commitSync();
            } finally {
                log.info("Закрываем консьюмер снапшотов");
                snapshotConsumer.close();
            }
        }
    }
}
