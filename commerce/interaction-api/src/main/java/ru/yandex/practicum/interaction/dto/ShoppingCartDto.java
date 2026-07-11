package ru.yandex.practicum.interaction.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Корзина товаров покупателя.
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDto {

    /**
     * Идентификатор корзины.
     */
    @NotNull
    private UUID shoppingCartId;

    /**
     * Отображение идентификатора товара на количество.
     */
    @NotNull
    @Builder.Default
    private Map<UUID, Long> products = new HashMap<>();
}
