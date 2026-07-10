package ru.yandex.practicum.payment.service;

import ru.yandex.practicum.interaction.dto.OrderDto;
import ru.yandex.practicum.interaction.dto.PaymentDto;

import java.util.UUID;

public interface PaymentService {

    PaymentDto payment(OrderDto order);

    Double getTotalCost(OrderDto order);

    void paymentSuccess(UUID paymentId);

    Double productCost(OrderDto order);

    void paymentFailed(UUID paymentId);
}
