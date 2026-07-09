package ru.yandex.practicum.interaction.api;

import ru.yandex.practicum.interaction.dto.AddProductToWarehouseDto;
import ru.yandex.practicum.interaction.dto.AddressDto;
import ru.yandex.practicum.interaction.dto.BookedProductsDto;
import ru.yandex.practicum.interaction.dto.NewProductInWarehouseDto;
import ru.yandex.practicum.interaction.dto.ShoppingCartDto;

/**
 * Контракт склада интернет-магазина.
 */
public interface WarehouseOperations {

    /**
     * Регистрирует новый товар на складе.
     */
    void newProductInWarehouse(NewProductInWarehouseDto request);

    /**
     * Проверяет, достаточно ли товаров на складе для указанной корзины.
     */
    BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto shoppingCart);

    /**
     * Принимает на склад указанное количество товара.
     */
    void addProductToWarehouse(AddProductToWarehouseDto request);

    /**
     * Возвращает адрес склада для расчёта доставки.
     */
    AddressDto getWarehouseAddress();
}
