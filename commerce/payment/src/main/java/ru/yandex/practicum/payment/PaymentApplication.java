package ru.yandex.practicum.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.yandex.practicum.interaction.client.OrderClient;
import ru.yandex.practicum.interaction.client.ShoppingStoreClient;

/**
 * Точка входа сервиса оплаты.
 */
@SpringBootApplication
@EnableFeignClients(clients = {ShoppingStoreClient.class, OrderClient.class})
public class PaymentApplication {

    /**
     * Запускает приложение.
     */
    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
    }
}
