package ru.yandex.practicum.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.delivery.model.Delivery;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий доставок.
 */
public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {

    /**
     * Возвращает доставку по идентификатору заказа.
     */
    Optional<Delivery> findByOrderId(UUID orderId);
}
