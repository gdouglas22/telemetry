package ru.yandex.practicum.store.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction.api.ShoppingStoreOperations;
import ru.yandex.practicum.interaction.dto.ProductCategory;
import ru.yandex.practicum.interaction.dto.ProductDto;
import ru.yandex.practicum.interaction.dto.SetProductQuantityStateDto;
import ru.yandex.practicum.store.service.ShoppingStoreService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ShoppingStoreController implements ShoppingStoreOperations {

    private final ShoppingStoreService shoppingStoreService;

    @Override
    @GetMapping
    public Page<ProductDto> getProducts(@RequestParam ProductCategory category, Pageable pageable) {
        return shoppingStoreService.getProducts(category, pageable);
    }

    @Override
    @PutMapping
    public ProductDto createNewProduct(@RequestBody @Valid ProductDto productDto) {
        return shoppingStoreService.createNewProduct(productDto);
    }

    @Override
    @PostMapping
    public ProductDto updateProduct(@RequestBody @Valid ProductDto productDto) {
        return shoppingStoreService.updateProduct(productDto);
    }

    @Override
    @PostMapping("/removeProductFromStore")
    public boolean removeProductFromStore(@RequestBody UUID productId) {
        return shoppingStoreService.removeProductFromStore(productId);
    }

    @Override
    @PostMapping("/quantityState")
    public boolean setProductQuantityState(@RequestBody @Valid SetProductQuantityStateDto request) {
        return shoppingStoreService.setProductQuantityState(request);
    }

    @Override
    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable UUID productId) {
        return shoppingStoreService.getProduct(productId);
    }
}
