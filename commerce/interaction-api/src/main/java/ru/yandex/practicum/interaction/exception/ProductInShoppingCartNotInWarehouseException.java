package ru.yandex.practicum.interaction.exception;

/**
 * Товар из корзины отсутствует в базе данных склада.
 */
public class ProductInShoppingCartNotInWarehouseException extends RuntimeException {

    public ProductInShoppingCartNotInWarehouseException(String message) {
        super(message);
    }
}
