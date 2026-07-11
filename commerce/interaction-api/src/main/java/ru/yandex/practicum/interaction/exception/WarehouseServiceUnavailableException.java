package ru.yandex.practicum.interaction.exception;

/**
 * Сервис склада временно недоступен.
 */
public class WarehouseServiceUnavailableException extends RuntimeException {

    public WarehouseServiceUnavailableException(String message) {
        super(message);
    }
}
