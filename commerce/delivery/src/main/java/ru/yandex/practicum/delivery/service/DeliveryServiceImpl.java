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

    private static final double BASE_RATE = 5.0;
    private static final double FRAGILE_RATE = 0.2;
    private static final double WEIGHT_RATE = 0.3;
    private static final double VOLUME_RATE = 0.2;
    private static final double STREET_MISMATCH_RATE = 0.2;
    private static final String EXPENSIVE_WAREHOUSE_ADDRESS = "ADDRESS_2";

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

    private double warehouseMultiplier(Address fromAddress) {
        String street = Objects.toString(fromAddress.getStreet(), "");
        return street.contains(EXPENSIVE_WAREHOUSE_ADDRESS) ? 2 : 1;
    }

    private double weightOf(OrderDto order) {
        return order.getDeliveryWeight() == null ? 0 : order.getDeliveryWeight();
    }

    private double volumeOf(OrderDto order) {
        return order.getDeliveryVolume() == null ? 0 : order.getDeliveryVolume();
    }

    private boolean sameStreet(Delivery delivery) {
        return Objects.equals(delivery.getFromAddress().getStreet(), delivery.getToAddress().getStreet());
    }

    private Delivery getDeliveryOrThrow(UUID orderId) {
        return deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException(
                        "Доставка для заказа " + orderId + " не найдена"));
    }
}
