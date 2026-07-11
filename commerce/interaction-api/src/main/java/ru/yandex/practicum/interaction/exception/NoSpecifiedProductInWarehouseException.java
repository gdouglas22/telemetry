package ru.yandex.practicum.interaction.exception;

/**
 * Товар не зарегистрирован на складе.
 */
public class NoSpecifiedProductInWarehouseException extends RuntimeException {

    public NoSpecifiedProductInWarehouseException(String message) {
        super(message);
    }
}
