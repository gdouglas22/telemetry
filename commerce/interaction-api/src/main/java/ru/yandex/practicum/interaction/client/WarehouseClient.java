package ru.yandex.practicum.interaction.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.interaction.api.WarehouseOperations;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse", fallbackFactory = WarehouseClientFallbackFactory.class)
public interface WarehouseClient extends WarehouseOperations {
}
