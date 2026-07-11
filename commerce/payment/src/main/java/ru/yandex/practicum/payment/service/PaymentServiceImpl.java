package ru.yandex.practicum.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.client.OrderClient;
import ru.yandex.practicum.interaction.client.ShoppingStoreClient;
import ru.yandex.practicum.interaction.dto.OrderDto;
import ru.yandex.practicum.interaction.dto.PaymentDto;
import ru.yandex.practicum.interaction.dto.PaymentState;
import ru.yandex.practicum.interaction.exception.NoPaymentFoundException;
import ru.yandex.practicum.interaction.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.payment.model.Payment;
import ru.yandex.practicum.payment.repository.PaymentRepository;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private static final double FEE_RATE = 0.1;

    private final PaymentRepository paymentRepository;
    private final ShoppingStoreClient shoppingStoreClient;
    private final OrderClient orderClient;

    @Override
    public PaymentDto payment(OrderDto order) {
        if (order.getProductPrice() == null || order.getDeliveryPrice() == null || order.getTotalPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException(
                    "В заказе " + order.getOrderId() + " недостаточно информации для формирования оплаты");
        }
        Payment payment = paymentRepository.save(Payment.builder()
                .orderId(order.getOrderId())
                .productTotal(order.getProductPrice())
                .deliveryTotal(order.getDeliveryPrice())
                .feeTotal(order.getProductPrice() * FEE_RATE)
                .totalPayment(order.getTotalPrice())
                .state(PaymentState.PENDING)
                .build());
        log.info("Сформирована оплата {} по заказу {}", payment.getPaymentId(), order.getOrderId());
        return PaymentDto.builder()
                .paymentId(payment.getPaymentId())
                .totalPayment(payment.getTotalPayment())
                .deliveryTotal(payment.getDeliveryTotal())
                .feeTotal(payment.getFeeTotal())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Double getTotalCost(OrderDto order) {
        if (order.getDeliveryPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException(
                    "В заказе " + order.getOrderId() + " не указана стоимость доставки");
        }
        double productCost = productCost(order);
        return productCost + productCost * FEE_RATE + order.getDeliveryPrice();
    }

    @Override
    public void paymentSuccess(UUID paymentId) {
        Payment payment = getPaymentOrThrow(paymentId);
        payment.setState(PaymentState.SUCCESS);
        paymentRepository.save(payment);
        orderClient.payment(payment.getOrderId());
        log.info("Оплата {} по заказу {} прошла успешно", paymentId, payment.getOrderId());
    }

    @Override
    @Transactional(readOnly = true)
    public Double productCost(OrderDto order) {
        if (order.getProducts() == null || order.getProducts().isEmpty()) {
            throw new NotEnoughInfoInOrderToCalculateException(
                    "В заказе " + order.getOrderId() + " нет товаров для расчёта стоимости");
        }
        double cost = 0;
        for (Map.Entry<UUID, Long> entry : order.getProducts().entrySet()) {
            double price = shoppingStoreClient.getProduct(entry.getKey()).getPrice().doubleValue();
            cost += price * entry.getValue();
        }
        return cost;
    }

    @Override
    public void paymentFailed(UUID paymentId) {
        Payment payment = getPaymentOrThrow(paymentId);
        payment.setState(PaymentState.FAILED);
        paymentRepository.save(payment);
        orderClient.paymentFailed(payment.getOrderId());
        log.info("Оплата {} по заказу {} завершилась ошибкой", paymentId, payment.getOrderId());
    }

    /**
     * Возвращает оплату по идентификатору или выбрасывает исключение, если оплата не найдена.
     */
    private Payment getPaymentOrThrow(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoPaymentFoundException(
                        "Оплата с идентификатором " + paymentId + " не найдена"));
    }
}
