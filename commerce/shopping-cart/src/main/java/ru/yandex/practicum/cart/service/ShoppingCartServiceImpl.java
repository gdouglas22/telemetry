package ru.yandex.practicum.cart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.cart.mapper.ShoppingCartMapper;
import ru.yandex.practicum.cart.model.ShoppingCart;
import ru.yandex.practicum.cart.repository.ShoppingCartRepository;
import ru.yandex.practicum.interaction.client.WarehouseClient;
import ru.yandex.practicum.interaction.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.interaction.dto.ShoppingCartDto;
import ru.yandex.practicum.interaction.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.interaction.exception.NotAuthorizedUserException;
import ru.yandex.practicum.interaction.exception.ShoppingCartDeactivatedException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final WarehouseClient warehouseClient;

    @Override
    public ShoppingCartDto getShoppingCart(String username) {
        checkUsername(username);
        return shoppingCartMapper.toDto(getOrCreateCart(username));
    }

    @Override
    public ShoppingCartDto addProductToShoppingCart(String username, Map<UUID, Long> products) {
        checkUsername(username);
        ShoppingCart cart = getOrCreateCart(username);
        checkCartActive(cart);
        cart.getProducts().putAll(products);

        ShoppingCartDto cartDto = shoppingCartMapper.toDto(cart);
        warehouseClient.checkProductQuantityEnoughForShoppingCart(cartDto);

        shoppingCartRepository.save(cart);
        log.info("В корзину пользователя {} добавлены товары: {}", username, products.keySet());
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public void deactivateCurrentShoppingCart(String username) {
        checkUsername(username);
        ShoppingCart cart = getOrCreateCart(username);
        cart.setActive(false);
        shoppingCartRepository.save(cart);
        log.info("Корзина пользователя {} деактивирована", username);
    }

    @Override
    public ShoppingCartDto removeFromShoppingCart(String username, List<UUID> products) {
        checkUsername(username);
        ShoppingCart cart = getOrCreateCart(username);
        checkCartActive(cart);
        List<UUID> missingProducts = products.stream()
                .filter(productId -> !cart.getProducts().containsKey(productId))
                .toList();
        if (!missingProducts.isEmpty()) {
            throw new NoProductsInShoppingCartException("В корзине нет товаров: " + missingProducts);
        }
        products.forEach(cart.getProducts()::remove);
        shoppingCartRepository.save(cart);
        log.info("Из корзины пользователя {} удалены товары: {}", username, products);
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public ShoppingCartDto changeProductQuantity(String username, ChangeProductQuantityRequest request) {
        checkUsername(username);
        ShoppingCart cart = getOrCreateCart(username);
        checkCartActive(cart);
        if (!cart.getProducts().containsKey(request.getProductId())) {
            throw new NoProductsInShoppingCartException(
                    "В корзине нет товара с идентификатором " + request.getProductId());
        }
        if (request.getNewQuantity() == 0) {
            cart.getProducts().remove(request.getProductId());
        } else {
            cart.getProducts().put(request.getProductId(), request.getNewQuantity());

            ShoppingCartDto cartDto = shoppingCartMapper.toDto(cart);
            warehouseClient.checkProductQuantityEnoughForShoppingCart(cartDto);
        }
        shoppingCartRepository.save(cart);
        return shoppingCartMapper.toDto(cart);
    }

    private ShoppingCart getOrCreateCart(String username) {
        return shoppingCartRepository.findByUsername(username)
                .orElseGet(() -> shoppingCartRepository.save(ShoppingCart.builder()
                        .username(username)
                        .active(true)
                        .build()));
    }

    private void checkUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым");
        }
    }

    private void checkCartActive(ShoppingCart cart) {
        if (!cart.isActive()) {
            throw new ShoppingCartDeactivatedException(
                    "Корзина пользователя " + cart.getUsername() + " деактивирована и недоступна для изменения");
        }
    }
}
