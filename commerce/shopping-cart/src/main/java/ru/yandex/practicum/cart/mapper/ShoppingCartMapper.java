package ru.yandex.practicum.cart.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.cart.model.ShoppingCart;
import ru.yandex.practicum.interaction.dto.ShoppingCartDto;

import java.util.HashMap;

/**
 * Преобразование корзины покупателя в DTO.
 */
@Component
public class ShoppingCartMapper {

    /**
     * Преобразует сущность корзины в DTO.
     */
    public ShoppingCartDto toDto(ShoppingCart shoppingCart) {
        return ShoppingCartDto.builder()
                .shoppingCartId(shoppingCart.getShoppingCartId())
                .products(new HashMap<>(shoppingCart.getProducts()))
                .build();
    }
}
