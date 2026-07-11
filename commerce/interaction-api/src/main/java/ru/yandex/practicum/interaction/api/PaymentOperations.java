package ru.yandex.practicum.interaction.api;

import ru.yandex.practicum.interaction.dto.OrderDto;
import ru.yandex.practicum.interaction.dto.PaymentDto;

import java.util.UUID;

/**
 * Контракт сервиса оплаты — адаптера платёжного шлюза.
 */
public interface PaymentOperations {

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
