package ru.yandex.practicum.store.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction.api.ShoppingStoreOperations;
import ru.yandex.practicum.interaction.dto.ProductCategory;
import ru.yandex.practicum.interaction.dto.ProductDto;
import ru.yandex.practicum.interaction.dto.QuantityState;
import ru.yandex.practicum.store.service.ShoppingStoreService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ShoppingStoreController implements ShoppingStoreOperations {

    private final ShoppingStoreService shoppingStoreService;

    @Override
    public List<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
        return shoppingStoreService.getProducts(category, pageable);
    }

    @Override
    public ProductDto createNewProduct(ProductDto productDto) {
        return shoppingStoreService.createNewProduct(productDto);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        return shoppingStoreService.updateProduct(productDto);
    }

    @Override
    public boolean removeProductFromStore(UUID productId) {
        return shoppingStoreService.removeProductFromStore(productId);
    }

    @Override
    public boolean setProductQuantityState(UUID productId, QuantityState quantityState) {
        return shoppingStoreService.setProductQuantityState(productId, quantityState);
    }

    @Override
    public ProductDto getProduct(UUID productId) {
        return shoppingStoreService.getProduct(productId);
    }
}
