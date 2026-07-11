package ru.yandex.practicum.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.cart.model.ShoppingCart;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий корзин покупателей.
 */
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, UUID> {

    /**
     * Возвращает корзину пользователя.
     */
    Optional<ShoppingCart> findByUsername(String username);
}
