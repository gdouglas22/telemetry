package ru.yandex.practicum.cart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.interaction.client.WarehouseClientFallbackFactory;

@Configuration
public class FeignConfig {

    @Bean
    public WarehouseClientFallbackFactory warehouseClientFallbackFactory() {
        return new WarehouseClientFallbackFactory();
    }
}
