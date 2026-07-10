package ru.yandex.practicum.interaction.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDto {

    @NotNull
    private UUID shoppingCartId;

    @NotNull
    @Builder.Default
    private Map<UUID, Long> products = new HashMap<>();
}
