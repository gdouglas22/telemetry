package ru.yandex.practicum.interaction.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.interaction.api.ShoppingStoreOperations;
import ru.yandex.practicum.interaction.dto.ProductCategory;
import ru.yandex.practicum.interaction.dto.ProductDto;
import ru.yandex.practicum.interaction.dto.SetProductQuantityStateDto;

import java.util.UUID;

/**
 * Feign-клиент витрины товаров.
 */
@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreClient extends ShoppingStoreOperations {

    @Override
    @GetMapping
    Page<ProductDto> getProducts(@RequestParam("category") ProductCategory category, Pageable pageable);

    @Override
    @PutMapping
    ProductDto createNewProduct(@RequestBody ProductDto productDto);

    @Override
    @PostMapping
    ProductDto updateProduct(@RequestBody ProductDto productDto);

    @Override
    @PostMapping("/removeProductFromStore")
    boolean removeProductFromStore(@RequestBody UUID productId);

    @Override
    @PostMapping("/quantityState")
    boolean setProductQuantityState(@RequestBody SetProductQuantityStateDto request);

    @Override
    @GetMapping("/{productId}")
    ProductDto getProduct(@PathVariable("productId") UUID productId);
}
