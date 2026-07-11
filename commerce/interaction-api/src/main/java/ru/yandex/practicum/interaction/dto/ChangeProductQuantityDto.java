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
 * Запрос на изменение количества единиц товара в корзине.
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeProductQuantityDto {

    /**
     * Идентификатор товара.
     */
    @NotNull
    private UUID productId;

    /**
     * Новое количество товара.
     */
    @NotNull
    @Min(0)
    private Long newQuantity;
}
