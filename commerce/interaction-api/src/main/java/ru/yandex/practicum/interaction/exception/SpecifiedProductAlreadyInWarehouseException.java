package ru.yandex.practicum.interaction.exception;

/**
 * Товар уже зарегистрирован на складе.
 */
public class SpecifiedProductAlreadyInWarehouseException extends RuntimeException {

    public SpecifiedProductAlreadyInWarehouseException(String message) {
        super(message);
    }
}
