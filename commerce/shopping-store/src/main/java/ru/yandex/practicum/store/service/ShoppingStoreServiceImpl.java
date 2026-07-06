package ru.yandex.practicum.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.dto.ProductCategory;
import ru.yandex.practicum.interaction.dto.ProductDto;
import ru.yandex.practicum.interaction.dto.ProductState;
import ru.yandex.practicum.interaction.dto.QuantityState;
import ru.yandex.practicum.interaction.exception.ProductNotFoundException;
import ru.yandex.practicum.store.mapper.ProductMapper;
import ru.yandex.practicum.store.model.Product;
import ru.yandex.practicum.store.repository.ProductRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingStoreServiceImpl implements ShoppingStoreService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getProducts(ProductCategory category, Pageable pageable) {
        return productRepository.findAllByProductCategory(category, pageable).stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    public ProductDto createNewProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        if (product.getProductState() == null) {
            product.setProductState(ProductState.ACTIVE);
        }
        Product saved = productRepository.save(product);
        log.info("Добавлен новый товар {} с идентификатором {}", saved.getProductName(), saved.getProductId());
        return productMapper.toDto(saved);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        Product product = getProductOrThrow(productDto.getProductId());
        product.setProductName(productDto.getProductName());
        product.setDescription(productDto.getDescription());
        product.setImageSrc(productDto.getImageSrc());
        product.setQuantityState(productDto.getQuantityState());
        product.setProductState(productDto.getProductState());
        product.setProductCategory(productDto.getProductCategory());
        product.setPrice(productDto.getPrice());
        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    public boolean removeProductFromStore(UUID productId) {
        Product product = getProductOrThrow(productId);
        product.setProductState(ProductState.DEACTIVATE);
        productRepository.save(product);
        log.info("Товар {} переведён в состояние DEACTIVATE", productId);
        return true;
    }

    @Override
    public boolean setProductQuantityState(UUID productId, QuantityState quantityState) {
        Product product = getProductOrThrow(productId);
        product.setQuantityState(quantityState);
        productRepository.save(product);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProduct(UUID productId) {
        return productMapper.toDto(getProductOrThrow(productId));
    }

    private Product getProductOrThrow(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Товар с идентификатором " + productId + " не найден"));
    }
}
