package ru.yandex.practicum.warehouse.service;

import ru.yandex.practicum.interaction.dto.AddProductToWarehouseDto;
import ru.yandex.practicum.interaction.dto.AddressDto;
import ru.yandex.practicum.interaction.dto.AssemblyProductsForOrderDto;
import ru.yandex.practicum.interaction.dto.BookedProductsDto;
import ru.yandex.practicum.interaction.dto.NewProductInWarehouseDto;
import ru.yandex.practicum.interaction.dto.ShippedToDeliveryDto;
import ru.yandex.practicum.interaction.dto.ShoppingCartDto;

import java.util.Map;
import java.util.UUID;

/**
 * Сервис склада.
 */
public interface WarehouseService {

    /**
     * Регистрирует новый товар на складе.
     */
    void newProductInWarehouse(NewProductInWarehouseDto request);

    /**
     * Связывает собранный заказ с доставкой.
     */
    void shippedToDelivery(ShippedToDeliveryDto request);

    /**
     * Принимает возврат товаров на склад, увеличивая доступный остаток.
     */
    void acceptReturn(Map<UUID, Long> products);

    /**
     * Проверяет, достаточно ли товаров на складе для указанной корзины.
     */
    BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto shoppingCart);

    /**
     * Собирает товары для заказа, уменьшая доступный остаток.
     */
    BookedProductsDto assemblyProductsForOrder(AssemblyProductsForOrderDto request);

    /**
     * Принимает на склад указанное количество товара.
     */
    void addProductToWarehouse(AddProductToWarehouseDto request);

    /**
     * Возвращает адрес склада для расчёта доставки.
     */
    AddressDto getWarehouseAddress();
}
