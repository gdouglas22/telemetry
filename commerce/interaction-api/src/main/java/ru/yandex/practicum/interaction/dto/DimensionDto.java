package ru.yandex.practicum.interaction.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Размеры товара.
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DimensionDto {

    /**
     * Ширина.
     */
    @NotNull
    @DecimalMin("1")
    private Double width;

    /**
     * Высота.
     */
    @NotNull
    @DecimalMin("1")
    private Double height;

    /**
     * Глубина.
     */
    @NotNull
    @DecimalMin("1")
    private Double depth;
}
