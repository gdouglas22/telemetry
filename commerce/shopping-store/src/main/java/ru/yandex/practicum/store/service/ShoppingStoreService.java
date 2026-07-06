package ru.yandex.practicum.store.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.interaction.dto.ProductCategory;
import ru.yandex.practicum.interaction.dto.ProductDto;
import ru.yandex.practicum.interaction.dto.QuantityState;

import java.util.List;
import java.util.UUID;

public interface ShoppingStoreService {

    List<ProductDto> getProducts(ProductCategory category, Pageable pageable);

    ProductDto createNewProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    boolean removeProductFromStore(UUID productId);

    boolean setProductQuantityState(UUID productId, QuantityState quantityState);

    ProductDto getProduct(UUID productId);
}
