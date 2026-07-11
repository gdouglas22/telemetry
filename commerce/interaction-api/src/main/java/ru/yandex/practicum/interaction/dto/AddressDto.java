package ru.yandex.practicum.interaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Представление адреса в системе.
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    /**
     * Страна.
     */
    private String country;

    /**
     * Город.
     */
    private String city;

    /**
     * Улица.
     */
    private String street;

    /**
     * Дом.
     */
    private String house;

    /**
     * Квартира.
     */
    private String flat;
}
