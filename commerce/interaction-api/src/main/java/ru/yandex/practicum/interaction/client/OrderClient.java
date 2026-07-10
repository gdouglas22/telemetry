package ru.yandex.practicum.interaction.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.interaction.api.OrderOperations;
import ru.yandex.practicum.interaction.dto.CreateNewOrderDto;
import ru.yandex.practicum.interaction.dto.OrderDto;
import ru.yandex.practicum.interaction.dto.ProductReturnDto;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderClient extends OrderOperations {

    @Override
    @GetMapping
    List<OrderDto> getClientOrders(@RequestParam("username") String username);

    @Override
    @PutMapping
    OrderDto createNewOrder(@RequestParam("username") String username, @RequestBody CreateNewOrderDto request);

    @Override
    @PostMapping("/return")
    OrderDto productReturn(@RequestBody ProductReturnDto request);

    @Override
    @PostMapping("/payment")
    OrderDto payment(@RequestBody UUID orderId);

    @Override
    @PostMapping("/payment/failed")
    OrderDto paymentFailed(@RequestBody UUID orderId);

    @Override
    @PostMapping("/delivery")
    OrderDto delivery(@RequestBody UUID orderId);

    @Override
    @PostMapping("/delivery/failed")
    OrderDto deliveryFailed(@RequestBody UUID orderId);

    @Override
    @PostMapping("/completed")
    OrderDto complete(@RequestBody UUID orderId);

    @Override
    @PostMapping("/calculate/total")
    OrderDto calculateTotalCost(@RequestBody UUID orderId);

    @Override
    @PostMapping("/calculate/delivery")
    OrderDto calculateDeliveryCost(@RequestBody UUID orderId);

    @Override
    @PostMapping("/assembly")
    OrderDto assembly(@RequestBody UUID orderId);

    @Override
    @PostMapping("/assembly/failed")
    OrderDto assemblyFailed(@RequestBody UUID orderId);
}
