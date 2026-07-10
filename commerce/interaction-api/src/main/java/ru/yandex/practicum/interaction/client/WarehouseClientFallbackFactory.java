package ru.yandex.practicum.interaction.client;

import org.springframework.cloud.openfeign.FallbackFactory;

public class WarehouseClientFallbackFactory implements FallbackFactory<WarehouseClient> {

    @Override
    public WarehouseClient create(Throwable cause) {
        return new WarehouseClientFallback(cause);
    }
}
