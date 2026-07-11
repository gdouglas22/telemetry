package ru.yandex.practicum.interaction.exception;

/**
 * В заказе недостаточно информации для расчёта.
 */
public class NotEnoughInfoInOrderToCalculateException extends RuntimeException {

    public NotEnoughInfoInOrderToCalculateException(String message) {
        super(message);
    }
}
