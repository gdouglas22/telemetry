package ru.yandex.practicum.store.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.interaction.exception.ApiErrorResponse;
import ru.yandex.practicum.interaction.exception.ProductNotFoundException;

@Slf4j
@RestControllerAdvice
public class ShoppingStoreErrorHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleProductNotFound(ProductNotFoundException e) {
        log.warn("Товар не найден: {}", e.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, "Товар не найден в витрине", e.getMessage());
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
