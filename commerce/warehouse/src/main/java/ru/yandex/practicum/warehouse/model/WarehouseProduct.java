package ru.yandex.practicum.warehouse.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Товар на складе.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseProduct {

    /**
     * Идентификатор товара.
     */
    @Id
    @Column(name = "product_id")
    private UUID productId;

    /**
     * Признак хрупкости.
     */
    private Boolean fragile;

    /**
     * Ширина.
     */
    @Column(nullable = false)
    private Double width;

    /**
     * Высота.
     */
    @Column(nullable = false)
    private Double height;

    /**
     * Глубина.
     */
    @Column(nullable = false)
    private Double depth;

    /**
     * Вес товара.
     */
    @Column(nullable = false)
    private Double weight;

    /**
     * Количество единиц товара.
     */
    @Column(nullable = false)
    private Long quantity;
}
