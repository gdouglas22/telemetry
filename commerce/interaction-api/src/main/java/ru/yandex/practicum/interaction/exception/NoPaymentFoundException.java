package ru.yandex.practicum.interaction.exception;

/**
 * Оплата не найдена.
 */
public class NoPaymentFoundException extends RuntimeException {

    public NoPaymentFoundException(String message) {
        super(message);
    }
}
