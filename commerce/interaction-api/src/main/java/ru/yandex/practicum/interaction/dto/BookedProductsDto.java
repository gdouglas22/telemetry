package ru.yandex.practicum.interaction.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookedProductsDto {

    @NotNull
    private Double deliveryWeight;

    @NotNull
    private Double deliveryVolume;

    @NotNull
    private Boolean fragile;
}
