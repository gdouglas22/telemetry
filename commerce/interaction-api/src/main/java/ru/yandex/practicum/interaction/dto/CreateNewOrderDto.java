package ru.yandex.practicum.interaction.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Запрос на создание нового заказа.
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNewOrderDto {

    /**
     * Корзина товаров для оформления заказа.
     */
    @NotNull
    @Valid
    private ShoppingCartDto shoppingCart;

    /**
     * Адрес доставки заказа.
     */
    @NotNull
    private AddressDto deliveryAddress;
}
