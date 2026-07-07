package ru.yandex.practicum.warehouse.service;

import ru.yandex.practicum.interaction.dto.AddProductToWarehouseRequest;
import ru.yandex.practicum.interaction.dto.AddressDto;
import ru.yandex.practicum.interaction.dto.BookedProductsDto;
import ru.yandex.practicum.interaction.dto.NewProductInWarehouseRequest;
import ru.yandex.practicum.interaction.dto.ShoppingCartDto;

public interface WarehouseService {

    void newProductInWarehouse(NewProductInWarehouseRequest request);

    BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto shoppingCart);

    void addProductToWarehouse(AddProductToWarehouseRequest request);

    AddressDto getWarehouseAddress();
}
