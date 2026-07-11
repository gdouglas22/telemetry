package ru.yandex.practicum.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.client.DeliveryClient;
import ru.yandex.practicum.interaction.client.PaymentClient;
import ru.yandex.practicum.interaction.client.WarehouseClient;
import ru.yandex.practicum.interaction.dto.AddressDto;
import ru.yandex.practicum.interaction.dto.AssemblyProductsForOrderDto;
import ru.yandex.practicum.interaction.dto.BookedProductsDto;
import ru.yandex.practicum.interaction.dto.CreateNewOrderDto;
import ru.yandex.practicum.interaction.dto.DeliveryDto;
import ru.yandex.practicum.interaction.dto.DeliveryState;
import ru.yandex.practicum.interaction.dto.OrderDto;
import ru.yandex.practicum.interaction.dto.OrderState;
import ru.yandex.practicum.interaction.dto.PaymentDto;
import ru.yandex.practicum.interaction.dto.ProductReturnDto;
import ru.yandex.practicum.interaction.exception.NoOrderFoundException;
import ru.yandex.practicum.interaction.exception.NotAuthorizedUserException;
import ru.yandex.practicum.order.mapper.OrderMapper;
import ru.yandex.practicum.order.model.Order;
import ru.yandex.practicum.order.repository.OrderRepository;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final WarehouseClient warehouseClient;
    private final DeliveryClient deliveryClient;
    private final PaymentClient paymentClient;

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getClientOrders(String username) {
        checkUsername(username);
        return orderRepository.findAllByUsername(username).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderDto createNewOrder(String username, CreateNewOrderDto request) {
        BookedProductsDto bookedProducts =
                warehouseClient.checkProductQuantityEnoughForShoppingCart(request.getShoppingCart());

        Order order = orderRepository.save(Order.builder()
                .username(username)
                .shoppingCartId(request.getShoppingCart().getShoppingCartId())
                .products(new HashMap<>(request.getShoppingCart().getProducts()))
                .state(OrderState.NEW)
                .deliveryWeight(bookedProducts.getDeliveryWeight())
                .deliveryVolume(bookedProducts.getDeliveryVolume())
                .fragile(bookedProducts.getFragile())
                .build());

        order.setDeliveryId(planDelivery(order.getOrderId(), request.getDeliveryAddress()));
        orderRepository.save(order);
        log.info("Создан заказ {} по корзине {}", order.getOrderId(), order.getShoppingCartId());
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto productReturn(ProductReturnDto request) {
        Order order = getOrderOrThrow(request.getOrderId());
        warehouseClient.acceptReturn(request.getProducts());
        order.setState(OrderState.PRODUCT_RETURNED);
        log.info("Оформлен возврат товаров по заказу {}", order.getOrderId());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto payment(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        if (order.getPaymentId() == null) {
            PaymentDto payment = paymentClient.payment(orderMapper.toDto(order));
            order.setPaymentId(payment.getPaymentId());
            order.setState(OrderState.ON_PAYMENT);
            log.info("Для заказа {} сформирована оплата {}", orderId, payment.getPaymentId());
        } else {
            order.setState(OrderState.PAID);
            log.info("Заказ {} оплачен", orderId);
        }
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto paymentFailed(UUID orderId) {
        return changeState(orderId, OrderState.PAYMENT_FAILED);
    }

    @Override
    public OrderDto delivery(UUID orderId) {
        return changeState(orderId, OrderState.DELIVERED);
    }

    @Override
    public OrderDto deliveryFailed(UUID orderId) {
        return changeState(orderId, OrderState.DELIVERY_FAILED);
    }

    @Override
    public OrderDto complete(UUID orderId) {
        return changeState(orderId, OrderState.COMPLETED);
    }

    @Override
    public OrderDto calculateTotalCost(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        order.setProductPrice(paymentClient.productCost(orderMapper.toDto(order)));
        order.setTotalPrice(paymentClient.getTotalCost(orderMapper.toDto(order)));
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto calculateDeliveryCost(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        order.setDeliveryPrice(deliveryClient.deliveryCost(orderMapper.toDto(order)));
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto assembly(UUID orderId) {
        Order order = getOrderOrThrow(orderId);
        BookedProductsDto bookedProducts = warehouseClient.assemblyProductsForOrder(
                AssemblyProductsForOrderDto.builder()
                        .orderId(orderId)
                        .products(new HashMap<>(order.getProducts()))
                        .build());
        order.setDeliveryWeight(bookedProducts.getDeliveryWeight());
        order.setDeliveryVolume(bookedProducts.getDeliveryVolume());
        order.setFragile(bookedProducts.getFragile());
        order.setState(OrderState.ASSEMBLED);
        log.info("Заказ {} собран на складе", orderId);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto assemblyFailed(UUID orderId) {
        return changeState(orderId, OrderState.ASSEMBLY_FAILED);
    }

    /**
     * Планирует доставку заказа от адреса склада до адреса покупателя.
     */
    private UUID planDelivery(UUID orderId, AddressDto deliveryAddress) {
        AddressDto warehouseAddress = warehouseClient.getWarehouseAddress();
        DeliveryDto delivery = deliveryClient.planDelivery(DeliveryDto.builder()
                .fromAddress(warehouseAddress)
                .toAddress(deliveryAddress)
                .orderId(orderId)
                .deliveryState(DeliveryState.CREATED)
                .build());
        return delivery.getDeliveryId();
    }

    /**
     * Переводит заказ в указанное состояние.
     */
    private OrderDto changeState(UUID orderId, OrderState state) {
        Order order = getOrderOrThrow(orderId);
        order.setState(state);
        log.info("Заказ {} переведён в состояние {}", orderId, state);
        return orderMapper.toDto(orderRepository.save(order));
    }

    /**
     * Возвращает заказ по идентификатору или выбрасывает исключение, если заказ не найден.
     */
    private Order getOrderOrThrow(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Заказ с идентификатором " + orderId + " не найден"));
    }

    /**
     * Проверяет, что имя пользователя не пустое.
     */
    private void checkUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым");
        }
    }
}
