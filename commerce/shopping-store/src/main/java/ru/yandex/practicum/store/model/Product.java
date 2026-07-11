package ru.yandex.practicum.store.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.practicum.interaction.dto.ProductCategory;
import ru.yandex.practicum.interaction.dto.ProductState;
import ru.yandex.practicum.interaction.dto.QuantityState;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Товар на витрине магазина.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    /**
     * Идентификатор товара.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id")
    private UUID productId;

    /**
     * Наименование товара.
     */
    @Column(name = "product_name", nullable = false)
    private String productName;

    /**
     * Описание товара.
     */
    @Column(nullable = false)
    private String description;

    /**
     * Ссылка на изображение товара.
     */
    @Column(name = "image_src")
    private String imageSrc;

    /**
     * Статус остатка товара.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "quantity_state", nullable = false)
    private QuantityState quantityState;

    /**
     * Статус товара в магазине.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "product_state", nullable = false)
    private ProductState productState;

    /**
     * Категория товара.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "product_category")
    private ProductCategory productCategory;

    /**
     * Цена товара.
     */
    @Column(nullable = false)
    private BigDecimal price;
}
