package ru.yandex.practicum.store.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.interaction.dto.ProductCategory;
import ru.yandex.practicum.store.model.Product;

import java.util.UUID;

/**
 * Репозиторий товаров витрины.
 */
public interface ProductRepository extends JpaRepository<Product, UUID> {

    /**
     * Возвращает страницу товаров указанной категории.
     */
    Page<Product> findAllByProductCategory(ProductCategory category, Pageable pageable);
}
