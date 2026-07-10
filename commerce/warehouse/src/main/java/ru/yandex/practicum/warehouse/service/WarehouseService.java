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

public interface WarehouseService {

    void newProductInWarehouse(NewProductInWarehouseDto request);

    void shippedToDelivery(ShippedToDeliveryDto request);

    void acceptReturn(Map<UUID, Long> products);

    BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto shoppingCart);

    BookedProductsDto assemblyProductsForOrder(AssemblyProductsForOrderDto request);

    void addProductToWarehouse(AddProductToWarehouseDto request);

    AddressDto getWarehouseAddress();
}
