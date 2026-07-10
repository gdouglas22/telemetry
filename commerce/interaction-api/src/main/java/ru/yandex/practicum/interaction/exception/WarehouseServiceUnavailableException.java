package ru.yandex.practicum.interaction.exception;

public class WarehouseServiceUnavailableException extends RuntimeException {

    public WarehouseServiceUnavailableException(String message) {
        super(message);
    }
}
