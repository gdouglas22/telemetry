package ru.yandex.practicum.interaction.exception;

/**
 * Искомые товары отсутствуют в корзине.
 */
public class NoProductsInShoppingCartException extends RuntimeException {

    public NoProductsInShoppingCartException(String message) {
        super(message);
    }
}
