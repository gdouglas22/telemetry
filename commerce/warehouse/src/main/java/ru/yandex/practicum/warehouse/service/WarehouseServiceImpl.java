package ru.yandex.practicum.warehouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interaction.dto.AddProductToWarehouseDto;
import ru.yandex.practicum.interaction.dto.AddressDto;
import ru.yandex.practicum.interaction.dto.AssemblyProductsForOrderDto;
import ru.yandex.practicum.interaction.dto.BookedProductsDto;
import ru.yandex.practicum.interaction.dto.NewProductInWarehouseDto;
import ru.yandex.practicum.interaction.dto.ShippedToDeliveryDto;
import ru.yandex.practicum.interaction.dto.ShoppingCartDto;
import ru.yandex.practicum.interaction.exception.NoOrderFoundException;
import ru.yandex.practicum.interaction.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.interaction.exception.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.yandex.practicum.interaction.exception.ProductInShoppingCartNotInWarehouseException;
import ru.yandex.practicum.interaction.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.warehouse.model.OrderBooking;
import ru.yandex.practicum.warehouse.model.WarehouseProduct;
import ru.yandex.practicum.warehouse.repository.OrderBookingRepository;
import ru.yandex.practicum.warehouse.repository.WarehouseProductRepository;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
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
    private final OrderBookingRepository orderBookingRepository;

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
    public void shippedToDelivery(ShippedToDeliveryDto request) {
        OrderBooking booking = orderBookingRepository.findById(request.getOrderId())
                .orElseThrow(() -> new NoOrderFoundException(
                        "Заказ " + request.getOrderId() + " не найден среди собранных на складе"));
        booking.setDeliveryId(request.getDeliveryId());
        orderBookingRepository.save(booking);
        log.info("Заказ {} передан в доставку {}", request.getOrderId(), request.getDeliveryId());
    }

    @Override
    public void acceptReturn(Map<UUID, Long> products) {
        for (Map.Entry<UUID, Long> entry : products.entrySet()) {
            WarehouseProduct product = getProductOrThrow(entry.getKey());
            product.setQuantity(product.getQuantity() + entry.getValue());
            warehouseProductRepository.save(product);
        }
        log.info("На склад возвращены товары: {}", products.keySet());
    }

    @Override
    @Transactional(readOnly = true)
    public BookedProductsDto checkProductQuantityEnoughForShoppingCart(ShoppingCartDto shoppingCart) {
        Map<UUID, WarehouseProduct> warehouseProducts = loadProducts(shoppingCart.getProducts().keySet());
        checkProductsExist(shoppingCart.getProducts(), warehouseProducts);
        checkQuantityEnough(shoppingCart.getProducts(), warehouseProducts);
        return buildBookedProducts(shoppingCart.getProducts(), warehouseProducts);
    }

    @Override
    public BookedProductsDto assemblyProductsForOrder(AssemblyProductsForOrderDto request) {
        Map<UUID, WarehouseProduct> warehouseProducts = loadProducts(request.getProducts().keySet());
        checkProductsExist(request.getProducts(), warehouseProducts);
        checkQuantityEnough(request.getProducts(), warehouseProducts);

        request.getProducts().forEach((productId, quantity) -> {
            WarehouseProduct product = warehouseProducts.get(productId);
            product.setQuantity(product.getQuantity() - quantity);
        });
        warehouseProductRepository.saveAll(warehouseProducts.values());

        orderBookingRepository.save(OrderBooking.builder()
                .orderId(request.getOrderId())
                .products(new HashMap<>(request.getProducts()))
                .build());
        log.info("Для заказа {} собраны товары: {}", request.getOrderId(), request.getProducts().keySet());
        return buildBookedProducts(request.getProducts(), warehouseProducts);
    }

    @Override
    public void addProductToWarehouse(AddProductToWarehouseDto request) {
        WarehouseProduct product = getProductOrThrow(request.getProductId());
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

    /**
     * Загружает товары склада по идентификаторам.
     */
    private Map<UUID, WarehouseProduct> loadProducts(Set<UUID> productIds) {
        return warehouseProductRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, Function.identity()));
    }

    /**
     * Проверяет, что все товары зарегистрированы на складе.
     */
    private void checkProductsExist(Map<UUID, Long> products, Map<UUID, WarehouseProduct> warehouseProducts) {
        List<UUID> missingProducts = products.keySet().stream()
                .filter(productId -> !warehouseProducts.containsKey(productId))
                .toList();
        if (!missingProducts.isEmpty()) {
            throw new ProductInShoppingCartNotInWarehouseException(
                    "Товары не зарегистрированы на складе: " + missingProducts);
        }
    }

    /**
     * Проверяет, что доступного остатка достаточно для каждого товара.
     */
    private void checkQuantityEnough(Map<UUID, Long> products, Map<UUID, WarehouseProduct> warehouseProducts) {
        List<UUID> lackingProducts = products.entrySet().stream()
                .filter(entry -> warehouseProducts.get(entry.getKey()).getQuantity() < entry.getValue())
                .map(Map.Entry::getKey)
                .toList();
        if (!lackingProducts.isEmpty()) {
            throw new ProductInShoppingCartLowQuantityInWarehouseException(
                    "Товаров недостаточно на складе: " + lackingProducts);
        }
    }

    /**
     * Считает вес, объём и признак хрупкости по набору товаров.
     */
    private BookedProductsDto buildBookedProducts(Map<UUID, Long> products,
                                                  Map<UUID, WarehouseProduct> warehouseProducts) {
        double deliveryWeight = 0;
        double deliveryVolume = 0;
        boolean fragile = false;
        for (Map.Entry<UUID, Long> entry : products.entrySet()) {
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

    /**
     * Возвращает товар по идентификатору или выбрасывает исключение, если товар не найден.
     */
    private WarehouseProduct getProductOrThrow(UUID productId) {
        return warehouseProductRepository.findById(productId)
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException(
                        "Товар с идентификатором " + productId + " не зарегистрирован на складе"));
    }
}
