package ru.yandex.practicum.interaction.exception;

/**
 * Товар из корзины не находится в требуемом количестве на складе.
 */
public class ProductInShoppingCartLowQuantityInWarehouseException extends RuntimeException {

    public ProductInShoppingCartLowQuantityInWarehouseException(String message) {
        super(message);
    }
}
