package ru.yandex.practicum.cart.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.cart.service.ShoppingCartService;
import ru.yandex.practicum.interaction.api.ShoppingCartOperations;
import ru.yandex.practicum.interaction.dto.ChangeProductQuantityDto;
import ru.yandex.practicum.interaction.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartController implements ShoppingCartOperations {

    private final ShoppingCartService shoppingCartService;

    @Override
    @GetMapping
    public ShoppingCartDto getShoppingCart(@RequestParam(required = false) String username) {
        return shoppingCartService.getShoppingCart(username);
    }

    @Override
    @PutMapping
    public ShoppingCartDto addProductToShoppingCart(@RequestParam(required = false) String username,
                                                    @RequestBody Map<UUID, Long> products) {
        return shoppingCartService.addProductToShoppingCart(username, products);
    }

    @Override
    @DeleteMapping
    public void deactivateCurrentShoppingCart(@RequestParam(required = false) String username) {
        shoppingCartService.deactivateCurrentShoppingCart(username);
    }

    @Override
    @PostMapping("/remove")
    public ShoppingCartDto removeFromShoppingCart(@RequestParam(required = false) String username,
                                                  @RequestBody List<UUID> products) {
        return shoppingCartService.removeFromShoppingCart(username, products);
    }

    @Override
    @PostMapping("/change-quantity")
    public ShoppingCartDto changeProductQuantity(@RequestParam(required = false) String username,
                                                 @RequestBody @Valid ChangeProductQuantityDto request) {
        return shoppingCartService.changeProductQuantity(username, request);
    }
}
