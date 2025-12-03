package com.raz.payment.domain.model.client;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.raz.payment.domain.model.account.Account;
import com.raz.payment.domain.model.operation.AccountOperation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OperationDetail {
    private Long id;
    private Account account;
    private AccountOperation operation;
    private BigDecimal accountBalanceBeforeOperation;
    private BigDecimal amount;
    private BigDecimal accountBalanceAfterOperation;
    private OperationDetailType operationDetailType;
    private LocalDateTime date;
}
