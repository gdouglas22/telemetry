package ru.yandex.practicum.interaction.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

/**
 * Запрос на добавление нового товара на склад.
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewProductInWarehouseDto {

    /**
     * Идентификатор товара.
     */
    @NotNull
    private UUID productId;

    /**
     * Признак хрупкости.
     */
    private Boolean fragile;

    /**
     * Размеры товара.
     */
    @NotNull
    @Valid
    private DimensionDto dimension;

    /**
     * Вес товара.
     */
    @NotNull
    @DecimalMin("1")
    private Double weight;
}
