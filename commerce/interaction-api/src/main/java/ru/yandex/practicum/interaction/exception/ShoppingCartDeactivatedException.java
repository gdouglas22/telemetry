package ru.yandex.practicum.interaction.exception;

public class ShoppingCartDeactivatedException extends RuntimeException {

    public ShoppingCartDeactivatedException(String message) {
        super(message);
    }
}
