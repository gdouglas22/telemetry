package ru.yandex.practicum.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.yandex.practicum.interaction.client.WarehouseClient;

/**
 * Точка входа сервиса корзины покупателя.
 */
@SpringBootApplication
@EnableFeignClients(clients = WarehouseClient.class)
public class ShoppingCartApplication {

    /**
     * Запускает приложение.
     */
    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartApplication.class, args);
    }
}
