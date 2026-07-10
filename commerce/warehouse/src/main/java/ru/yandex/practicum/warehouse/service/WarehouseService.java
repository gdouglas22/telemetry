package ru.yandex.practicum.warehouse.service;

import ru.yandex.practicum.interaction.dto.AddProductToWarehouseDto;
import ru.yandex.practicum.interaction.dto.AddressDto;
import ru.yandex.practicum.interaction.dto.BookedProductsDto;
import ru.yandex.practicum.interaction.dto.NewProductInWarehouseDto;
import ru.yandex.practicum.interaction.dto.ShoppingCartDto;

public interface WarehouseService {

    void newProductInWarehouse(NewProductInWarehouseDto request);

    BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto shoppingCart);

    void addProductToWarehouse(AddProductToWarehouseDto request);

    AddressDto getWarehouseAddress();
}
