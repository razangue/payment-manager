package com.raz.payment.domain.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class<?> entityClass, Object id) {
        super(entityClass.getSimpleName() + " with ID " + id + " was not found.");
    }
}
