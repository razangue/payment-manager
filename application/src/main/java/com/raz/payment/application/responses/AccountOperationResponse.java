package com.raz.payment.application.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountOperationResponse {
    private Long id;
    private AccountResponse account;
    private BigDecimal amount;
    private LocalDateTime date;
    private String accountOperationType;
}
