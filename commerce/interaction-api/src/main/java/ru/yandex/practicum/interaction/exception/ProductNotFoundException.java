package ru.yandex.practicum.interaction.exception;

/**
 * Товар не найден в витрине магазина.
 */
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }
}
