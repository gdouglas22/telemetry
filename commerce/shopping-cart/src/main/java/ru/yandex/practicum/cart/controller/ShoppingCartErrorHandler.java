package ru.yandex.practicum.cart.controller;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.interaction.exception.ApiErrorResponse;
import ru.yandex.practicum.interaction.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.interaction.exception.NotAuthorizedUserException;
import ru.yandex.practicum.interaction.exception.ShoppingCartDeactivatedException;
import ru.yandex.practicum.interaction.exception.WarehouseServiceUnavailableException;

@Slf4j
@RestControllerAdvice
public class ShoppingCartErrorHandler {

    @ExceptionHandler(NotAuthorizedUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiErrorResponse handleNotAuthorizedUser(NotAuthorizedUserException e) {
        log.warn("Запрос без имени пользователя: {}", e.getMessage());
        return buildResponse(HttpStatus.UNAUTHORIZED, "Имя пользователя не указано", e.getMessage());
    }

    @ExceptionHandler(NoProductsInShoppingCartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleNoProductsInCart(NoProductsInShoppingCartException e) {
        log.warn("Товары не найдены в корзине: {}", e.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Искомые товары отсутствуют в корзине", e.getMessage());
    }

    @ExceptionHandler(ShoppingCartDeactivatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleCartDeactivated(ShoppingCartDeactivatedException e) {
        log.warn("Попытка изменить деактивированную корзину: {}", e.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Корзина деактивирована", e.getMessage());
    }

    @ExceptionHandler(WarehouseServiceUnavailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ApiErrorResponse handleWarehouseUnavailable(WarehouseServiceUnavailableException e) {
        log.warn("Сервис склада недоступен: {}", e.getMessage());
        return buildResponse(HttpStatus.SERVICE_UNAVAILABLE, "Сервис склада временно недоступен", e.getMessage());
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleFeignException(FeignException e) {
        log.warn("Ошибка при обращении к сервису склада: {}", e.getMessage());
        HttpStatus status = HttpStatus.resolve(e.status());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(e.contentUTF8());
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
                .httpStatus(status.name())
                .userMessage(userMessage)
                .message(message)
                .build();
    }
}
