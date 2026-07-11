package ru.yandex.practicum.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.yandex.practicum.interaction.client.OrderClient;
import ru.yandex.practicum.interaction.client.WarehouseClient;

/**
 * Точка входа сервиса доставки.
 */
@SpringBootApplication
@EnableFeignClients(clients = {OrderClient.class, WarehouseClient.class})
public class DeliveryApplication {

    /**
     * Запускает приложение.
     */
    public static void main(String[] args) {
        SpringApplication.run(DeliveryApplication.class, args);
    }
}
