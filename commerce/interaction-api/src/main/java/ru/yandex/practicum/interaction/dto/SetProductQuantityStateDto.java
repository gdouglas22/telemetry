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
 * Запрос на изменение статуса остатка товара.
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetProductQuantityStateDto {

    /**
     * Идентификатор товара.
     */
    @NotNull
    private UUID productId;

    /**
     * Статус остатка товара.
     */
    @NotNull
    private QuantityState quantityState;
}
