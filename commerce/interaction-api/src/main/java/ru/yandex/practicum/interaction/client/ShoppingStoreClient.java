package ru.yandex.practicum.interaction.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.interaction.api.ShoppingStoreOperations;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreClient extends ShoppingStoreOperations {
}
