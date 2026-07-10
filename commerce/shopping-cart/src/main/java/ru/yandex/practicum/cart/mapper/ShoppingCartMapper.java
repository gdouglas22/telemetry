package ru.yandex.practicum.cart.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.cart.model.ShoppingCart;
import ru.yandex.practicum.interaction.dto.ShoppingCartDto;

import java.util.HashMap;

@Component
public class ShoppingCartMapper {

    public ShoppingCartDto toDto(ShoppingCart shoppingCart) {
        return ShoppingCartDto.builder()
                .shoppingCartId(shoppingCart.getShoppingCartId())
                .products(new HashMap<>(shoppingCart.getProducts()))
                .build();
    }
}
