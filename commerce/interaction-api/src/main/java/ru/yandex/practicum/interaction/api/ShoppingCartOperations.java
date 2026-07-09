package ru.yandex.practicum.interaction.api;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.interaction.dto.ChangeProductQuantityDto;
import ru.yandex.practicum.interaction.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartOperations {

    @GetMapping
    ShoppingCartDto getShoppingCart(@RequestParam(required = false) String username);

    @PutMapping
    ShoppingCartDto addProductToShoppingCart(@RequestParam(required = false) String username,
                                             @RequestBody Map<UUID, Long> products);

    @DeleteMapping
    void deactivateCurrentShoppingCart(@RequestParam(required = false) String username);

    @PostMapping("/remove")
    ShoppingCartDto removeFromShoppingCart(@RequestParam(required = false) String username,
                                           @RequestBody List<UUID> products);

    @PostMapping("/change-quantity")
    ShoppingCartDto changeProductQuantity(@RequestParam(required = false) String username,
                                          @RequestBody @Valid ChangeProductQuantityDto request);
}
