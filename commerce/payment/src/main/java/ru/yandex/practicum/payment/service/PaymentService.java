package ru.yandex.practicum.payment.service;

import ru.yandex.practicum.interaction.dto.OrderDto;
import ru.yandex.practicum.interaction.dto.PaymentDto;

import java.util.UUID;

/**
 * Сервис оплаты заказов.
 */
public interface PaymentService {

    /**
     * Формирует и сохраняет сведения об оплате заказа.
     */
    PaymentDto payment(OrderDto order);

    /**
     * Рассчитывает итоговую стоимость заказа с учётом доставки и налога.
     */
    Double getTotalCost(OrderDto order);

    /**
     * Отмечает успешную оплату, полученную от платёжного шлюза.
     */
    void paymentSuccess(UUID paymentId);

    /**
     * Рассчитывает стоимость товаров заказа.
     */
    Double productCost(OrderDto order);

    /**
     * Отмечает неудачную оплату, полученную от платёжного шлюза.
     */
    void paymentFailed(UUID paymentId);
}
