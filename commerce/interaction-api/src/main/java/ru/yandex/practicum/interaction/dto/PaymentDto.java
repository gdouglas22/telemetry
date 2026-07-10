package ru.yandex.practicum.interaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    private UUID paymentId;

    private Double totalPayment;

    private Double deliveryTotal;

    private Double feeTotal;
}
