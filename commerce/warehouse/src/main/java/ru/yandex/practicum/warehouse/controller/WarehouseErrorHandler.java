package ru.yandex.practicum.warehouse.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.interaction.exception.ApiErrorResponse;
import ru.yandex.practicum.interaction.exception.NoOrderFoundException;
import ru.yandex.practicum.interaction.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.interaction.exception.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.yandex.practicum.interaction.exception.ProductInShoppingCartNotInWarehouseException;
import ru.yandex.practicum.interaction.exception.SpecifiedProductAlreadyInWarehouseException;

@Slf4j
@RestControllerAdvice
public class WarehouseErrorHandler {

    @ExceptionHandler(SpecifiedProductAlreadyInWarehouseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleProductAlreadyInWarehouse(SpecifiedProductAlreadyInWarehouseException e) {
        log.warn("Товар уже зарегистрирован на складе: {}", e.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Товар уже зарегистрирован на складе", e.getMessage());
    }

    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleNoProductInWarehouse(NoSpecifiedProductInWarehouseException e) {
        log.warn("Товар не зарегистрирован на складе: {}", e.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Нет информации о товаре на складе", e.getMessage());
    }

    @ExceptionHandler(ProductInShoppingCartNotInWarehouseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleProductNotInWarehouse(ProductInShoppingCartNotInWarehouseException e) {
        log.warn("Товар из корзины отсутствует на складе: {}", e.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Товар из корзины отсутствует в БД склада", e.getMessage());
    }

    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleLowQuantity(ProductInShoppingCartLowQuantityInWarehouseException e) {
        log.warn("Недостаточно товара на складе: {}", e.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST,
                "Товар из корзины не находится в требуемом количестве на складе", e.getMessage());
    }

    @ExceptionHandler(NoOrderFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleNoOrderFound(NoOrderFoundException e) {
        log.warn("Заказ не найден на складе: {}", e.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, "Заказ не найден", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleValidation(MethodArgumentNotValidException e) {
        log.warn("Ошибка валидации запроса: {}", e.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Некорректный запрос", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse handleUnexpected(Exception e) {
        log.error("Непредвиденная ошибка", e);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервиса", e.getMessage());
    }

    private ApiErrorResponse buildResponse(HttpStatus status, String userMessage, String message) {
        return ApiErrorResponse.builder()
                .httpStatus(status.toString())
                .userMessage(userMessage)
                .message(message)
                .build();
    }
}
