package ru.yandex.practicum.interaction.exception;

/**
 * Имя пользователя в запросе не указано.
 */
public class NotAuthorizedUserException extends RuntimeException {

    public NotAuthorizedUserException(String message) {
        super(message);
    }
}
