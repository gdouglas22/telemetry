package ru.yandex.practicum.order.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.practicum.interaction.dto.OrderState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Заказ пользователя.
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    /**
     * Идентификатор заказа.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    private UUID orderId;

    /**
     * Имя пользователя.
     */
    private String username;

    /**
     * Идентификатор корзины.
     */
    @Column(name = "shopping_cart_id")
    private UUID shoppingCartId;

    /**
     * Отображение идентификатора товара на количество.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity", nullable = false)
    @Builder.Default
    private Map<UUID, Long> products = new HashMap<>();

    /**
     * Идентификатор оплаты.
     */
    @Column(name = "payment_id")
    private UUID paymentId;

    /**
     * Идентификатор доставки.
     */
    @Column(name = "delivery_id")
    private UUID deliveryId;

    /**
     * Статус заказа.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderState state;

    /**
     * Общий вес доставки.
     */
    @Column(name = "delivery_weight")
    private Double deliveryWeight;

    /**
     * Общий объём доставки.
     */
    @Column(name = "delivery_volume")
    private Double deliveryVolume;

    /**
     * Признак хрупкости.
     */
    private Boolean fragile;

    /**
     * Общая стоимость заказа.
     */
    @Column(name = "total_price")
    private Double totalPrice;

    /**
     * Стоимость доставки.
     */
    @Column(name = "delivery_price")
    private Double deliveryPrice;

    /**
     * Стоимость товаров в заказе.
     */
    @Column(name = "product_price")
    private Double productPrice;
}
