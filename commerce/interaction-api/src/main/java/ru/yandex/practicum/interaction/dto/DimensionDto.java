package ru.yandex.practicum.interaction.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    @Positive
    private Double width;

    @NotNull
    @Positive
    private Double height;

    @NotNull
    @Positive
    private Double depth;
}
