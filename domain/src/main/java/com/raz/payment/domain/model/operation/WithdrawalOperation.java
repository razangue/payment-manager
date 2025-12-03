package com.raz.payment.domain.model.operation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.raz.payment.domain.model.account.Account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class WithdrawalOperation extends AccountOperation {
    public WithdrawalOperation(Account sourceAccount, BigDecimal amount, LocalDateTime date) {
        super(sourceAccount, amount, date, AccountOperationType.WITHDRAWAL);
    }
}
