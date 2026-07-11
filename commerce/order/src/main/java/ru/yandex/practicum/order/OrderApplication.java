package ru.yandex.practicum.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.yandex.practicum.interaction.client.DeliveryClient;
import ru.yandex.practicum.interaction.client.PaymentClient;
import ru.yandex.practicum.interaction.client.WarehouseClient;

/**
 * Точка входа сервиса заказов.
 */
@SpringBootApplication
@EnableFeignClients(clients = {WarehouseClient.class, DeliveryClient.class, PaymentClient.class})
public class OrderApplication {

    /**
     * Запускает приложение.
     */
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
