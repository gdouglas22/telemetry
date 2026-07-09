package ru.yandex.practicum.interaction.api;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.interaction.dto.AddProductToWarehouseDto;
import ru.yandex.practicum.interaction.dto.AddressDto;
import ru.yandex.practicum.interaction.dto.BookedProductsDto;
import ru.yandex.practicum.interaction.dto.NewProductInWarehouseDto;
import ru.yandex.practicum.interaction.dto.ShoppingCartDto;

public interface WarehouseOperations {

    @PutMapping
    void newProductInWarehouse(@RequestBody @Valid NewProductInWarehouseDto request);

    @PostMapping("/check")
    BookedProductsDto checkProductQuantityEnoughForShoppingCart(@RequestBody @Valid ShoppingCartDto shoppingCart);

    @PostMapping("/add")
    void addProductToWarehouse(@RequestBody @Valid AddProductToWarehouseDto request);

    @GetMapping("/address")
    AddressDto getWarehouseAddress();
}
