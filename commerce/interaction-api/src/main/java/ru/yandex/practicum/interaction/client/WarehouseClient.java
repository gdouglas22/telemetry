package ru.yandex.practicum.interaction.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.interaction.api.WarehouseOperations;
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
 * Feign-клиент склада.
 */
@FeignClient(name = "warehouse", path = "/api/v1/warehouse", fallbackFactory = WarehouseClientFallbackFactory.class)
public interface WarehouseClient extends WarehouseOperations {

    @Override
    @PutMapping
    void newProductInWarehouse(@RequestBody NewProductInWarehouseDto request);

    @Override
    @PostMapping("/shipped")
    void shippedToDelivery(@RequestBody ShippedToDeliveryDto request);

    @Override
    @PostMapping("/return")
    void acceptReturn(@RequestBody Map<UUID, Long> products);

    @Override
    @PostMapping("/check")
    BookedProductsDto checkProductQuantityEnoughForShoppingCart(@RequestBody ShoppingCartDto shoppingCart);

    @Override
    @PostMapping("/assembly")
    BookedProductsDto assemblyProductsForOrder(@RequestBody AssemblyProductsForOrderDto request);

    @Override
    @PostMapping("/add")
    void addProductToWarehouse(@RequestBody AddProductToWarehouseDto request);

    @Override
    @GetMapping("/address")
    AddressDto getWarehouseAddress();
}
