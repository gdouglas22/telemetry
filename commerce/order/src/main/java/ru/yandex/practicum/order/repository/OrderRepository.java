package ru.yandex.practicum.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.order.model.Order;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий заказов.
 */
public interface OrderRepository extends JpaRepository<Order, UUID> {

    /**
     * Возвращает все заказы пользователя.
     */
    List<Order> findAllByUsername(String username);
}
