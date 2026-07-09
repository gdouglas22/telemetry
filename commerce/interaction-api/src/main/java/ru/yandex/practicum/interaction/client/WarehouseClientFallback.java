package ru.yandex.practicum.interaction.client;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.interaction.dto.AddProductToWarehouseDto;
import ru.yandex.practicum.interaction.dto.AddressDto;
import ru.yandex.practicum.interaction.dto.BookedProductsDto;
import ru.yandex.practicum.interaction.dto.NewProductInWarehouseDto;
import ru.yandex.practicum.interaction.dto.ShoppingCartDto;
import ru.yandex.practicum.interaction.exception.WarehouseServiceUnavailableException;

@RequiredArgsConstructor
public class WarehouseClientFallback implements WarehouseClient {

    private final Throwable cause;

    @Override
    public void newProductInWarehouse(NewProductInWarehouseDto request) {
        throw translate();
    }

    @Override
    public BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto shoppingCart) {
        throw translate();
    }

    @Override
    public void addProductToWarehouse(AddProductToWarehouseDto request) {
        throw translate();
    }

    @Override
    public AddressDto getWarehouseAddress() {
        throw translate();
    }

    private RuntimeException translate() {
        if (cause instanceof FeignException.FeignClientException clientException) {
            return clientException;
        }
        return new WarehouseServiceUnavailableException(
                "Сервис склада временно недоступен. Попробуйте повторить запрос позже.");
    }
}
