package ru.yandex.practicum.interaction.exception;

public class ProductInShoppingCartNotInWarehouseException extends RuntimeException {

    public ProductInShoppingCartNotInWarehouseException(String message) {
        super(message);
    }
}
