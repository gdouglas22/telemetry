package ru.yandex.practicum.interaction.exception;

public class ProductInShoppingCartLowQuantityInWarehouseException extends RuntimeException {

    public ProductInShoppingCartLowQuantityInWarehouseException(String message) {
        super(message);
    }
}
