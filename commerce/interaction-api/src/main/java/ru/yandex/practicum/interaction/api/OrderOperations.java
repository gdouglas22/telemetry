package ru.yandex.practicum.interaction.api;

import ru.yandex.practicum.interaction.dto.CreateNewOrderDto;
import ru.yandex.practicum.interaction.dto.OrderDto;
import ru.yandex.practicum.interaction.dto.ProductReturnDto;

import java.util.List;
import java.util.UUID;

/**
 * Контракт сервиса заказов интернет-магазина.
 */
public interface OrderOperations {

    /**
     * Возвращает список заказов пользователя.
     */
    List<OrderDto> getClientOrders(String username);

    /**
     * Создаёт новый заказ на основе корзины и адреса доставки.
     */
    OrderDto createNewOrder(String username, CreateNewOrderDto request);

    /**
     * Оформляет возврат товаров заказа.
     */
    OrderDto productReturn(ProductReturnDto request);

    /**
     * Отмечает заказ оплаченным, при необходимости формируя сведения об оплате.
     */
    OrderDto payment(UUID orderId);

    /**
     * Отмечает неудачную оплату заказа.
     */
    OrderDto paymentFailed(UUID orderId);

    /**
     * Отмечает заказ доставленным.
     */
    OrderDto delivery(UUID orderId);

    /**
     * Отмечает неудачную доставку заказа.
     */
    OrderDto deliveryFailed(UUID orderId);

    /**
     * Завершает выполненный заказ.
     */
    OrderDto complete(UUID orderId);

    /**
     * Рассчитывает итоговую стоимость заказа.
     */
    OrderDto calculateTotalCost(UUID orderId);

    /**
     * Рассчитывает стоимость доставки заказа.
     */
    OrderDto calculateDeliveryCost(UUID orderId);

    /**
     * Запускает сборку заказа на складе.
     */
    OrderDto assembly(UUID orderId);

    /**
     * Отмечает неудачную сборку заказа.
     */
    OrderDto assemblyFailed(UUID orderId);
}
