package com.raz.payment.application.requests;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record InternalOperationRequest(@NotNull String accountNumber,
                @NotNull BigDecimal amount) {
}
