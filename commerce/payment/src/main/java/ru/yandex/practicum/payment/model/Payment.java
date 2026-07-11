package ru.yandex.practicum.payment.model;

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
import ru.yandex.practicum.interaction.dto.PaymentState;

import java.util.UUID;

/**
 * Оплата заказа.
 */
@Entity
@Table(name = "payments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    /**
     * Идентификатор оплаты.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    private UUID paymentId;

    /**
     * Идентификатор заказа.
     */
    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    /**
     * Стоимость товаров.
     */
    @Column(name = "product_total")
    private Double productTotal;

    /**
     * Стоимость доставки.
     */
    @Column(name = "delivery_total")
    private Double deliveryTotal;

    /**
     * Стоимость налога.
     */
    @Column(name = "fee_total")
    private Double feeTotal;

    /**
     * Общая стоимость оплаты.
     */
    @Column(name = "total_payment")
    private Double totalPayment;

    /**
     * Статус оплаты.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentState state;
}
