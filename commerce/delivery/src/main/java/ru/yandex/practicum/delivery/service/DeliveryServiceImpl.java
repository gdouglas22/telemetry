package ru.yandex.practicum.delivery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.delivery.mapper.DeliveryMapper;
import ru.yandex.practicum.delivery.model.Address;
import ru.yandex.practicum.delivery.model.Delivery;
import ru.yandex.practicum.delivery.repository.DeliveryRepository;
import ru.yandex.practicum.interaction.client.OrderClient;
import ru.yandex.practicum.interaction.client.WarehouseClient;
import ru.yandex.practicum.interaction.dto.DeliveryDto;
import ru.yandex.practicum.interaction.dto.DeliveryState;
import ru.yandex.practicum.interaction.dto.OrderDto;
import ru.yandex.practicum.interaction.dto.ShippedToDeliveryDto;
import ru.yandex.practicum.interaction.exception.NoDeliveryFoundException;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryServiceImpl implements DeliveryService {

    /**
     * Базовая стоимость доставки.
     */
    private static final double BASE_RATE = 5.0;

    /**
     * Надбавка за хрупкость заказа.
     */
    private static final double FRAGILE_RATE = 0.2;

    /**
     * Стоимость доставки килограмма веса.
     */
    private static final double WEIGHT_RATE = 0.3;

    /**
     * Стоимость доставки кубического метра объёма.
     */
    private static final double VOLUME_RATE = 0.2;

    /**
     * Надбавка за доставку на другую улицу.
     */
    private static final double STREET_MISMATCH_RATE = 0.2;

    /**
     * Название адреса склада с повышенным множителем стоимости.
     */
    private static final String EXPENSIVE_WAREHOUSE_ADDRESS = "ADDRESS_2";

    /**
     * Множитель базовой стоимости для склада с повышенным тарифом.
     */
    private static final double EXPENSIVE_ADDRESS_MULTIPLIER = 2;

    /**
     * Множитель базовой стоимости для остальных складов.
     */
    private static final double REGULAR_ADDRESS_MULTIPLIER = 1;

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    @Override
    public DeliveryDto planDelivery(DeliveryDto delivery) {
        Delivery saved = deliveryRepository.save(Delivery.builder()
                .orderId(delivery.getOrderId())
                .fromAddress(deliveryMapper.toAddress(delivery.getFromAddress()))
                .toAddress(deliveryMapper.toAddress(delivery.getToAddress()))
                .state(DeliveryState.CREATED)
                .build());
        log.info("Запланирована доставка {} для заказа {}", saved.getDeliveryId(), saved.getOrderId());
        return deliveryMapper.toDto(saved);
    }

    @Override
    public void deliverySuccessful(UUID orderId) {
        Delivery delivery = getDeliveryOrThrow(orderId);
        delivery.setState(DeliveryState.DELIVERED);
        deliveryRepository.save(delivery);
        orderClient.delivery(orderId);
        log.info("Доставка {} по заказу {} завершена", delivery.getDeliveryId(), orderId);
    }

    @Override
    public void deliveryPicked(UUID orderId) {
        Delivery delivery = getDeliveryOrThrow(orderId);
        delivery.setState(DeliveryState.IN_PROGRESS);
        deliveryRepository.save(delivery);
        warehouseClient.shippedToDelivery(ShippedToDeliveryDto.builder()
                .orderId(orderId)
                .deliveryId(delivery.getDeliveryId())
                .build());
        log.info("Заказ {} принят в доставку {}", orderId, delivery.getDeliveryId());
    }

    @Override
    public void deliveryFailed(UUID orderId) {
        Delivery delivery = getDeliveryOrThrow(orderId);
        delivery.setState(DeliveryState.FAILED);
        deliveryRepository.save(delivery);
        orderClient.deliveryFailed(orderId);
        log.info("Доставка {} по заказу {} завершилась ошибкой", delivery.getDeliveryId(), orderId);
    }

    @Override
    @Transactional(readOnly = true)
    public Double deliveryCost(OrderDto order) {
        Delivery delivery = getDeliveryOrThrow(order.getOrderId());

        double cost = BASE_RATE + BASE_RATE * warehouseMultiplier(delivery.getFromAddress());
        if (Boolean.TRUE.equals(order.getFragile())) {
            cost += cost * FRAGILE_RATE;
        }
        cost += weightOf(order) * WEIGHT_RATE;
        cost += volumeOf(order) * VOLUME_RATE;
        if (!sameStreet(delivery)) {
            cost += cost * STREET_MISMATCH_RATE;
        }
        return cost;
    }

    /**
     * Возвращает множитель стоимости в зависимости от адреса склада.
     */
    private double warehouseMultiplier(Address fromAddress) {
        String street = Objects.toString(fromAddress.getStreet(), "");
        return street.contains(EXPENSIVE_WAREHOUSE_ADDRESS)
                ? EXPENSIVE_ADDRESS_MULTIPLIER
                : REGULAR_ADDRESS_MULTIPLIER;
    }

    /**
     * Возвращает вес заказа или ноль, если вес не указан.
     */
    private double weightOf(OrderDto order) {
        return order.getDeliveryWeight() == null ? 0 : order.getDeliveryWeight();
    }

    /**
     * Возвращает объём заказа или ноль, если объём не указан.
     */
    private double volumeOf(OrderDto order) {
        return order.getDeliveryVolume() == null ? 0 : order.getDeliveryVolume();
    }

    /**
     * Проверяет, совпадает ли улица доставки с улицей склада.
     */
    private boolean sameStreet(Delivery delivery) {
        return Objects.equals(delivery.getFromAddress().getStreet(), delivery.getToAddress().getStreet());
    }

    /**
     * Возвращает доставку по заказу или выбрасывает исключение, если доставка не найдена.
     */
    private Delivery getDeliveryOrThrow(UUID orderId) {
        return deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException(
                        "Доставка для заказа " + orderId + " не найдена"));
    }
}
