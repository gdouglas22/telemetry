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
 * Представление доставки в системе.
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {

    /**
     * Идентификатор доставки.
     */
    private UUID deliveryId;

    /**
     * Адрес отправления.
     */
    @NotNull
    private AddressDto fromAddress;

    /**
     * Адрес назначения.
     */
    @NotNull
    private AddressDto toAddress;

    /**
     * Идентификатор заказа.
     */
    @NotNull
    private UUID orderId;

    /**
     * Статус доставки.
     */
    private DeliveryState deliveryState;
}
