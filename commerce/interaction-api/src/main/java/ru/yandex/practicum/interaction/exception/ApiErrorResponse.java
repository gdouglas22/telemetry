package ru.yandex.practicum.interaction.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Тело ответа сервиса об ошибке.
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {

    /**
     * HTTP-статус ошибки.
     */
    private String httpStatus;

    /**
     * Сообщение об ошибке для пользователя.
     */
    private String userMessage;

    /**
     * Детальное описание ошибки.
     */
    private String message;
}
