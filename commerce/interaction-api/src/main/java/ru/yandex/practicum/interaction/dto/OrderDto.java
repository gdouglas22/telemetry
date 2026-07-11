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
 * Представление заказа в системе.
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    /**
     * Идентификатор заказа.
     */
    @NotNull
    private UUID orderId;

    /**
     * Идентификатор корзины.
     */
    private UUID shoppingCartId;

    /**
     * Отображение идентификатора товара на количество.
     */
    @NotNull
    @Builder.Default
    private Map<UUID, Long> products = new HashMap<>();

    /**
     * Идентификатор оплаты.
     */
    private UUID paymentId;

    /**
     * Идентификатор доставки.
     */
    private UUID deliveryId;

    /**
     * Статус заказа.
     */
    private OrderState state;

    /**
     * Общий вес доставки.
     */
    private Double deliveryWeight;

    /**
     * Общий объём доставки.
     */
    private Double deliveryVolume;

    /**
     * Признак хрупкости.
     */
    private Boolean fragile;

    /**
     * Общая стоимость заказа.
     */
    private Double totalPrice;

    /**
     * Стоимость доставки.
     */
    private Double deliveryPrice;

    /**
     * Стоимость товаров в заказе.
     */
    private Double productPrice;
}
