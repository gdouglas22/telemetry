package ru.yandex.practicum.interaction.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.interaction.api.PaymentOperations;
import ru.yandex.practicum.interaction.dto.OrderDto;
import ru.yandex.practicum.interaction.dto.PaymentDto;

import java.util.UUID;

/**
 * Feign-клиент сервиса оплаты.
 */
@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentClient extends PaymentOperations {

    @Override
    @PostMapping
    PaymentDto payment(@RequestBody OrderDto order);

    @Override
    @PostMapping("/totalCost")
    Double getTotalCost(@RequestBody OrderDto order);

    @Override
    @PostMapping("/refund")
    void paymentSuccess(@RequestBody UUID paymentId);

    @Override
    @PostMapping("/productCost")
    Double productCost(@RequestBody OrderDto order);

    @Override
    @PostMapping("/failed")
    void paymentFailed(@RequestBody UUID paymentId);
}
