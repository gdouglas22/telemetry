package ru.yandex.practicum.interaction.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Товар, продаваемый в интернет-магазине.
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    /**
     * Идентификатор товара.
     */
    private UUID productId;

    /**
     * Наименование товара.
     */
    @NotBlank
    private String productName;

    /**
     * Описание товара.
     */
    @NotBlank
    private String description;

    /**
     * Ссылка на изображение товара.
     */
    private String imageSrc;

    /**
     * Статус остатка товара.
     */
    @NotNull
    private QuantityState quantityState;

    /**
     * Статус товара в магазине.
     */
    @NotNull
    private ProductState productState;

    /**
     * Категория товара.
     */
    private ProductCategory productCategory;

    /**
     * Цена товара.
     */
    @NotNull
    @Min(1)
    private BigDecimal price;
}
