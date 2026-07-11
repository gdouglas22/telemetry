package ru.yandex.practicum.interaction.exception;

/**
 * Заказ не найден.
 */
public class NoOrderFoundException extends RuntimeException {

    public NoOrderFoundException(String message) {
        super(message);
    }
}
