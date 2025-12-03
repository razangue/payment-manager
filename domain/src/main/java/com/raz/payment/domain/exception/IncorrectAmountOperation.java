package com.raz.payment.domain.exception;

import java.math.BigDecimal;

public class IncorrectAmountOperation extends RuntimeException {
    public IncorrectAmountOperation(BigDecimal amount) {
        super("Incorrect amount for operation :" + amount);
    }
}
