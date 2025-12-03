package com.raz.payment.domain.exception;

import com.raz.payment.domain.model.account.Account;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(Account account) {
        super(" Account with number " + account.getAccountNumber() + " has insufficient funds. Your account balance is: " + account.getCurrentBalance());
    }
}
