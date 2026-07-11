package ru.yandex.practicum.interaction.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

/**
 * Запрос на передачу собранного заказа в доставку.
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippedToDeliveryDto {

    /**
     * Идентификатор заказа.
     */
    @NotNull
    private UUID orderId;

    /**
     * Идентификатор доставки.
     */
    @NotNull
    private UUID deliveryId;
}
