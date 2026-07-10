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

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewProductInWarehouseDto {

    @NotNull
    private UUID productId;

    private Boolean fragile;

    @NotNull
    @Valid
    private DimensionDto dimension;

    @NotNull
    @DecimalMin("1")
    private Double weight;
}
