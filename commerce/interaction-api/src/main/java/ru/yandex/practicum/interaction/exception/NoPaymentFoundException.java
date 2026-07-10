package ru.yandex.practicum.interaction.exception;

public class NoPaymentFoundException extends RuntimeException {

    public NoPaymentFoundException(String message) {
        super(message);
    }
}
