package ru.yandex.practicum.interaction.dto;

import jakarta.validation.constraints.DecimalMin;
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
public class DimensionDto {

    @NotNull
    @DecimalMin("1")
    private Double width;

    @NotNull
    @DecimalMin("1")
    private Double height;

    @NotNull
    @DecimalMin("1")
    private Double depth;
}
