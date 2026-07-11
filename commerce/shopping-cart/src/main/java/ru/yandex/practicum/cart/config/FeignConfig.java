package ru.yandex.practicum.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.interaction.client.WarehouseClientFallbackFactory;

/**
 * Конфигурация Feign-клиентов корзины покупателя.
 */
@Configuration
public class FeignConfig {

    /**
     * Создаёт фабрику заглушек клиента склада.
     */
    @Bean
    public WarehouseClientFallbackFactory warehouseClientFallbackFactory() {
        return new WarehouseClientFallbackFactory();
    }
}
