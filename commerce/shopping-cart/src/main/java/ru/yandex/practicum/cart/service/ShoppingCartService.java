package ru.yandex.practicum.cart.service;

import ru.yandex.practicum.interaction.dto.ChangeProductQuantityDto;
import ru.yandex.practicum.interaction.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {

    ShoppingCartDto getShoppingCart(String username);

    ShoppingCartDto addProductToShoppingCart(String username, Map<UUID, Long> products);

    void deactivateCurrentShoppingCart(String username);

    ShoppingCartDto removeFromShoppingCart(String username, List<UUID> products);

    ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityDto request);
}
