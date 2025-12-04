package com.raz.payment.domain.exception;

public class UnknowOperationTypeException extends RuntimeException {
    public UnknowOperationTypeException() {
        super("Unknow operation type.");
    }
}