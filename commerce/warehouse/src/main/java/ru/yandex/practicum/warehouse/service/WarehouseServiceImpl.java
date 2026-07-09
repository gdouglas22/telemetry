package ru.yandex.practicum.warehouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.dto.AddProductToWarehouseDto;
import ru.yandex.practicum.interaction.dto.AddressDto;
import ru.yandex.practicum.interaction.dto.BookedProductsDto;
import ru.yandex.practicum.interaction.dto.NewProductInWarehouseDto;
import ru.yandex.practicum.interaction.dto.ShoppingCartDto;
import ru.yandex.practicum.interaction.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.interaction.exception.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.yandex.practicum.interaction.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.warehouse.model.WarehouseProduct;
import ru.yandex.practicum.warehouse.repository.WarehouseProductRepository;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class WarehouseServiceImpl implements WarehouseService {

    private static final String[] ADDRESSES = new String[] {"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];

    private final WarehouseProductRepository warehouseProductRepository;

    @Override
    public void newProductInWarehouse(NewProductInWarehouseDto request) {
        if (warehouseProductRepository.existsById(request.getProductId())) {
            throw new SpecifiedProductAlreadyInWarehouseException(
                    "Товар с идентификатором " + request.getProductId() + " уже зарегистрирован на складе");
        }
        WarehouseProduct product = WarehouseProduct.builder()
                .productId(request.getProductId())
                .fragile(Boolean.TRUE.equals(request.getFragile()))
                .width(request.getDimension().getWidth())
                .height(request.getDimension().getHeight())
                .depth(request.getDimension().getDepth())
                .weight(request.getWeight())
                .quantity(0L)
                .build();
        warehouseProductRepository.save(product);
        log.info("Товар {} зарегистрирован на складе", request.getProductId());
    }

    @Override
    @Transactional(readOnly = true)
    public BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto shoppingCart) {
        Map<UUID, Long> cartProducts = shoppingCart.getProducts();
        Map<UUID, WarehouseProduct> warehouseProducts =
                warehouseProductRepository.findAllById(cartProducts.keySet()).stream()
                        .collect(Collectors.toMap(WarehouseProduct::getProductId, Function.identity()));

        List<UUID> lackingProducts = cartProducts.entrySet().stream()
                .filter(entry -> {
                    WarehouseProduct product = warehouseProducts.get(entry.getKey());
                    return product == null || product.getQuantity() < entry.getValue();
                })
                .map(Map.Entry::getKey)
                .toList();
        if (!lackingProducts.isEmpty()) {
            throw new ProductInShoppingCartLowQuantityInWarehouseException(
                    "Товаров недостаточно на складе: " + lackingProducts);
        }

        double deliveryWeight = 0;
        double deliveryVolume = 0;
        boolean fragile = false;
        for (Map.Entry<UUID, Long> entry : cartProducts.entrySet()) {
            WarehouseProduct product = warehouseProducts.get(entry.getKey());
            deliveryWeight += product.getWeight() * entry.getValue();
            deliveryVolume += product.getWidth() * product.getHeight() * product.getDepth() * entry.getValue();
            fragile = fragile || Boolean.TRUE.equals(product.getFragile());
        }
        return BookedProductsDto.builder()
                .deliveryWeight(deliveryWeight)
                .deliveryVolume(deliveryVolume)
                .fragile(fragile)
                .build();
    }

    @Override
    public void addProductToWarehouse(AddProductToWarehouseDto request) {
        WarehouseProduct product = warehouseProductRepository.findById(request.getProductId())
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(
                        "Товар с идентификатором " + request.getProductId() + " не зарегистрирован на складе"));
        product.setQuantity(product.getQuantity() + request.getQuantity());
        warehouseProductRepository.save(product);
        log.info("На склад добавлено {} единиц товара {}", request.getQuantity(), request.getProductId());
    }

    @Override
    public AddressDto getWarehouseAddress() {
        return AddressDto.builder()
                .country(CURRENT_ADDRESS)
                .city(CURRENT_ADDRESS)
                .street(CURRENT_ADDRESS)
                .house(CURRENT_ADDRESS)
                .flat(CURRENT_ADDRESS)
                .build();
    }
}
