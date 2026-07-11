package ru.yandex.practicum.interaction.dto;

/**
 * Статус оплаты.
 */
public enum PaymentState {
    /**
     * Ожидает оплаты.
     */
    PENDING,
    /**
     * Успешно оплачен.
     */
    SUCCESS,
    /**
     * Ошибка в процессе оплаты.
     */
    FAILED
}
