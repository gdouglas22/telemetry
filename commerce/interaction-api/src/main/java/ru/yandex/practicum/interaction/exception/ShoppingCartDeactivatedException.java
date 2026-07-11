package ru.yandex.practicum.interaction.exception;

/**
 * Корзина деактивирована и недоступна для изменения.
 */
public class ShoppingCartDeactivatedException extends RuntimeException {

    public ShoppingCartDeactivatedException(String message) {
        super(message);
    }
}
