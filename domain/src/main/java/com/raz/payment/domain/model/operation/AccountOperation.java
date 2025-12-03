package com.raz.payment.domain.model.operation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.raz.payment.domain.model.account.Account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
@SuperBuilder
public abstract class AccountOperation {
    private Long id;
    private Account sourceAccount;
    private BigDecimal amount;
    private LocalDateTime date;
    private AccountOperationType accountOperationType;

    protected AccountOperation(Account sourceAccount, BigDecimal amount, LocalDateTime date, AccountOperationType accountOperationType) {
        this.sourceAccount = sourceAccount;
        this.amount = amount;
        this.date = date;
        this.accountOperationType = accountOperationType;
    }

}
