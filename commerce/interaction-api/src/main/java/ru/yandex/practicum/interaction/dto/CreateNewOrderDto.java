package ru.yandex.practicum.interaction.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNewOrderDto {

    @NotNull
    @Valid
    private ShoppingCartDto shoppingCart;

    @NotNull
    private AddressDto deliveryAddress;
}
