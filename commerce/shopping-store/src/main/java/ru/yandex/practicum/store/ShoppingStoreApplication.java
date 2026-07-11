package ru.yandex.practicum.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Точка входа сервиса витрины товаров.
 */
@SpringBootApplication
public class ShoppingStoreApplication {

    /**
     * Запускает приложение.
     */
    public static void main(String[] args) {
        SpringApplication.run(ShoppingStoreApplication.class, args);
    }
}
