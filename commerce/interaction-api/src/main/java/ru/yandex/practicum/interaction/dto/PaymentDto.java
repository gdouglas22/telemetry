package ru.yandex.practicum.interaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

/**
 * Сведения об оплате заказа.
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    /**
     * Идентификатор оплаты.
     */
    private UUID paymentId;

    /**
     * Общая стоимость оплаты.
     */
    private Double totalPayment;

    /**
     * Стоимость доставки.
     */
    private Double deliveryTotal;

    /**
     * Стоимость налога.
     */
    private Double feeTotal;
}
