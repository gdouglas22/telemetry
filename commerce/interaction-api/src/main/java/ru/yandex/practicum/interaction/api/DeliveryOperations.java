package ru.yandex.practicum.interaction.api;

import ru.yandex.practicum.interaction.dto.DeliveryDto;
import ru.yandex.practicum.interaction.dto.OrderDto;

import java.util.UUID;

/**
 * Контракт сервиса доставки заказов.
 */
public interface DeliveryOperations {

    /**
     * Сохраняет сведения о новой доставке заказа.
     */
    DeliveryDto planDelivery(DeliveryDto delivery);

    /**
     * Отмечает успешную доставку заказа.
     */
    void deliverySuccessful(UUID orderId);

    /**
     * Отмечает получение заказа службой доставки.
     */
    void deliveryPicked(UUID orderId);

    /**
     * Отмечает неудачную доставку заказа.
     */
    void deliveryFailed(UUID orderId);

    /**
     * Рассчитывает стоимость доставки заказа.
     */
    Double deliveryCost(OrderDto order);
}
