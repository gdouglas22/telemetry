package ru.yandex.practicum.aggregator.starter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.aggregator.config.KafkaProperties;
import ru.yandex.practicum.aggregator.service.SnapshotService;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;

/**
 * Класс, ответственный за запуск агрегации данных.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {

    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final KafkaProducer<String, SpecificRecordBase> producer;
    private final SnapshotService snapshotService;
    private final KafkaProperties properties;

    /**
     * Подписывается на топик с событиями датчиков, агрегирует их
     * в снимки состояния хабов и записывает обновлённые снимки в Kafka.
     */
    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
        try {
            consumer.subscribe(List.of(properties.getTopics().getSensors()));

            while (true) {
                ConsumerRecords<String, SensorEventAvro> records =
                        consumer.poll(Duration.ofMillis(properties.getPollTimeoutMs()));
                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    SensorEventAvro event = record.value();
                    snapshotService.updateState(event)
                            .ifPresent(this::sendSnapshot);
                }
                if (!records.isEmpty()) {
                    producer.flush();
                    consumer.commitSync();
                }
            }
        } catch (WakeupException ignored) {
            // игнорируем — закрываем консьюмер и продюсер в блоке finally
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            try {
                producer.flush();
                consumer.commitSync();
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
                producer.close();
            }
        }
    }

    private void sendSnapshot(SensorsSnapshotAvro snapshot) {
        log.info("Отправляем снапшот хаба {} в топик {}",
                snapshot.getHubId(), properties.getTopics().getSnapshots());
        producer.send(new ProducerRecord<>(
                properties.getTopics().getSnapshots(), snapshot.getHubId(), snapshot));
    }
}
