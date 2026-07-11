package ru.yandex.practicum.store.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.interaction.dto.ProductCategory;
import ru.yandex.practicum.interaction.dto.ProductDto;
import ru.yandex.practicum.interaction.dto.SetProductQuantityStateDto;

import java.util.UUID;

/**
 * Сервис витрины товаров.
 */
public interface ShoppingStoreService {

    /**
     * Возвращает страницу товаров указанной категории.
     */
    Page<ProductDto> getProducts(ProductCategory category, Pageable pageable);

    /**
     * Добавляет новый товар в ассортимент.
     */
    ProductDto createNewProduct(ProductDto productDto);

    /**
     * Обновляет данные существующего товара.
     */
    ProductDto updateProduct(ProductDto productDto);

    /**
     * Выводит товар из ассортимента, переводя его в состояние DEACTIVATE.
     */
    boolean removeProductFromStore(UUID productId);

    /**
     * Устанавливает статус остатка товара.
     */
    boolean setProductQuantityState(SetProductQuantityStateDto request);

    /**
     * Возвращает сведения о товаре по его идентификатору.
     */
    ProductDto getProduct(UUID productId);
}
