package ru.yandex.practicum.interaction.dto;

/**
 * Статус доставки.
 */
public enum DeliveryState {
    /**
     * Создана.
     */
    CREATED,
    /**
     * В процессе выполнения.
     */
    IN_PROGRESS,
    /**
     * Доставлена.
     */
    DELIVERED,
    /**
     * Неудачная доставка.
     */
    FAILED,
    /**
     * Отменена.
     */
    CANCELLED
}
