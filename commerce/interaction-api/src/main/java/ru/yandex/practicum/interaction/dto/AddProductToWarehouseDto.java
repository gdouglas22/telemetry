package ru.yandex.practicum.interaction.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

/**
 * Запрос на увеличение остатка товара на складе.
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddProductToWarehouseDto {

    /**
     * Идентификатор товара.
     */
    @NotNull
    private UUID productId;

    /**
     * Количество единиц товара.
     */
    @NotNull
    @Min(1)
    private Long quantity;
}
