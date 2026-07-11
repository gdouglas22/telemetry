package ru.yandex.practicum.interaction.exception;

/**
 * Доставка не найдена.
 */
public class NoDeliveryFoundException extends RuntimeException {

    public NoDeliveryFoundException(String message) {
        super(message);
    }
}
