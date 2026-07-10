package ru.yandex.practicum.analyzer.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.config.KafkaProperties;
import ru.yandex.practicum.analyzer.handler.HubEventHandler;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HubEventProcessor implements Runnable {

    private final KafkaConsumer<String, HubEventAvro> consumer;
    private final Map<Class<?>, HubEventHandler> handlers;
    private final KafkaProperties properties;

    public HubEventProcessor(KafkaConsumer<String, HubEventAvro> hubEventConsumer,
                             List<HubEventHandler> handlers,
                             KafkaProperties properties) {
        this.consumer = hubEventConsumer;
        this.handlers = handlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getPayloadType, Function.identity()));
        this.properties = properties;
    }

    @Override
    public void run() {
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
        try {
            consumer.subscribe(List.of(properties.getTopics().getHubs()));

            while (true) {
                ConsumerRecords<String, HubEventAvro> records =
                        consumer.poll(Duration.ofMillis(properties.getPollTimeoutMs()));
                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    handleEvent(record.value());
                }
            }
        } catch (WakeupException ignored) {
            // игнорируем — закрываем консьюмер в блоке finally
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий хабов", e);
        } finally {
            log.info("Закрываем консьюмер событий хабов");
            consumer.close();
        }
    }

    private void handleEvent(HubEventAvro event) {
        HubEventHandler handler = handlers.get(event.getPayload().getClass());
        if (handler == null) {
            log.warn("Не найден обработчик для события {}", event.getPayload().getClass().getSimpleName());
            return;
        }
        handler.handle(event);
    }
}
