package ru.yandex.practicum.payment.controller;

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
import ru.yandex.practicum.interaction.exception.NoPaymentFoundException;
import ru.yandex.practicum.interaction.exception.NotEnoughInfoInOrderToCalculateException;

/**
 * Обработчик ошибок сервиса оплаты.
 */
@Slf4j
@RestControllerAdvice
public class PaymentErrorHandler {

    /**
     * Обрабатывает отсутствие оплаты.
     */
    @ExceptionHandler(NoPaymentFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleNoPaymentFound(NoPaymentFoundException e) {
        log.warn("Оплата не найдена: {}", e.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, "Оплата не найдена", e.getMessage());
    }

    /**
     * Обрабатывает нехватку информации в заказе для расчёта.
     */
    @ExceptionHandler(NotEnoughInfoInOrderToCalculateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleNotEnoughInfo(NotEnoughInfoInOrderToCalculateException e) {
        log.warn("Недостаточно информации в заказе: {}", e.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Недостаточно информации в заказе для расчёта", e.getMessage());
    }

    /**
     * Пробрасывает ошибку смежного сервиса с исходным статусом.
     */
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleFeignException(FeignException e) {
        log.warn("Ошибка при обращении к смежному сервису: {}", e.getMessage());
        HttpStatus status = HttpStatus.resolve(e.status());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(e.contentUTF8());
    }

    /**
     * Обрабатывает ошибки валидации запроса.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleValidation(MethodArgumentNotValidException e) {
        log.warn("Ошибка валидации запроса: {}", e.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Некорректный запрос", e.getMessage());
    }

    /**
     * Обрабатывает непредвиденные ошибки.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse handleUnexpected(Exception e) {
        log.error("Непредвиденная ошибка", e);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервиса", e.getMessage());
    }

    /**
     * Формирует тело ответа об ошибке.
     */
    private ApiErrorResponse buildResponse(HttpStatus status, String userMessage, String message) {
        return ApiErrorResponse.builder()
                .httpStatus(status.toString())
                .userMessage(userMessage)
                .message(message)
                .build();
    }
}
