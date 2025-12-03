package com.raz.payment.domain.model.operation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.raz.payment.domain.model.account.Account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class PaymentOperation extends AccountOperation {
    private Account receivingAccount;

    public PaymentOperation(Account sourceAccount, Account receivingAccount, BigDecimal amount, LocalDateTime date) {
        super(sourceAccount, amount, date, AccountOperationType.PAYMENT);
        this.receivingAccount = receivingAccount;
    }
}
