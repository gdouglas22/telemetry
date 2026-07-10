package ru.yandex.practicum.interaction.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.interaction.api.DeliveryOperations;
import ru.yandex.practicum.interaction.dto.DeliveryDto;
import ru.yandex.practicum.interaction.dto.OrderDto;

import java.util.UUID;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryClient extends DeliveryOperations {

    @Override
    @PutMapping
    DeliveryDto planDelivery(@RequestBody DeliveryDto delivery);

    @Override
    @PostMapping("/successful")
    void deliverySuccessful(@RequestBody UUID orderId);

    @Override
    @PostMapping("/picked")
    void deliveryPicked(@RequestBody UUID orderId);

    @Override
    @PostMapping("/failed")
    void deliveryFailed(@RequestBody UUID orderId);

    @Override
    @PostMapping("/cost")
    Double deliveryCost(@RequestBody OrderDto order);
}
