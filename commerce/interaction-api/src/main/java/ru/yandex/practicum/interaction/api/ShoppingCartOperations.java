package ru.yandex.practicum.interaction.api;

import ru.yandex.practicum.interaction.dto.ChangeProductQuantityDto;
import ru.yandex.practicum.interaction.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Контракт корзины покупателя интернет-магазина.
 */
public interface ShoppingCartOperations {

    /**
     * Возвращает корзину пользователя, создавая новую при её отсутствии.
     */
    ShoppingCartDto getShoppingCart(String username);

    /**
     * Добавляет товары в корзину пользователя.
     */
    ShoppingCartDto addProductToShoppingCart(String username, Map<UUID, Long> products);

    /**
     * Деактивирует корзину пользователя.
     */
    void deactivateCurrentShoppingCart(String username);

    /**
     * Удаляет указанные товары из корзины пользователя.
     */
    ShoppingCartDto removeFromShoppingCart(String username, List<UUID> products);

    /**
     * Изменяет количество единиц товара в корзине пользователя.
     */
    ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityDto request);
}
