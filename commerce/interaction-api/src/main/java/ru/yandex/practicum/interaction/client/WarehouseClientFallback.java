package ru.yandex.practicum.interaction.client;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.interaction.dto.AddProductToWarehouseDto;
import ru.yandex.practicum.interaction.dto.AddressDto;
import ru.yandex.practicum.interaction.dto.AssemblyProductsForOrderDto;
import ru.yandex.practicum.interaction.dto.BookedProductsDto;
import ru.yandex.practicum.interaction.dto.NewProductInWarehouseDto;
import ru.yandex.practicum.interaction.dto.ShippedToDeliveryDto;
import ru.yandex.practicum.interaction.dto.ShoppingCartDto;
import ru.yandex.practicum.interaction.exception.WarehouseServiceUnavailableException;

import java.util.Map;
import java.util.UUID;

/**
 * Заглушка клиента склада, переводящая причину сбоя в понятное исключение.
 */
@RequiredArgsConstructor
public class WarehouseClientFallback implements WarehouseClient {

    private final Throwable cause;

    @Override
    public void newProductInWarehouse(NewProductInWarehouseDto request) {
        throw translate();
    }

    @Override
    public void shippedToDelivery(ShippedToDeliveryDto request) {
        throw translate();
    }

    @Override
    public void acceptReturn(Map<UUID, Long> products) {
        throw translate();
    }

    @Override
    public BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto shoppingCart) {
        throw translate();
    }

    @Override
    public BookedProductsDto assemblyProductsForOrder(AssemblyProductsForOrderDto request) {
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

    /**
     * Преобразует причину сбоя: ошибки клиента пробрасываются без изменений, остальное считается недоступностью склада.
     */
    private RuntimeException translate() {
        if (cause instanceof FeignException.FeignClientException clientException) {
            return clientException;
        }
        return new WarehouseServiceUnavailableException(
                "Сервис склада временно недоступен. Попробуйте повторить запрос позже.");
    }
}
