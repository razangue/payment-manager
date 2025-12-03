package com.raz.payment.application.requests;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PaymentOperationRequest(@NotNull String sourceAccountNumber,
        @NotNull BigDecimal amount, @NotNull String receivingAccountNumber) {
}
