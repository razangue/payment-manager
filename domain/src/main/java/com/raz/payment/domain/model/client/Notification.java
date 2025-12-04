package com.raz.payment.domain.model.client;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private Long id;
    private String accountNumber;
    private Long operationDetailId;
    private BigDecimal accountBalanceBeforeOperation;
    private BigDecimal amount;
    private BigDecimal accountBalanceAfterOperation;
    private OperationDetailType operationDetailType;
    private LocalDateTime date;
}
