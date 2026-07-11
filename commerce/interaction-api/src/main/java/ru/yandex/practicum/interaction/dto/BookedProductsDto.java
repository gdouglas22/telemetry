package ru.yandex.practicum.interaction.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Сведения о зарезервированных по корзине товарах.
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookedProductsDto {

    /**
     * Общий вес доставки.
     */
    @NotNull
    private Double deliveryWeight;

    /**
     * Общий объём доставки.
     */
    @NotNull
    private Double deliveryVolume;

    /**
     * Признак хрупкости.
     */
    @NotNull
    private Boolean fragile;
}
