package ru.yandex.practicum.interaction.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.interaction.api.ShoppingCartOperations;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface ShoppingCartClient extends ShoppingCartOperations {
}
