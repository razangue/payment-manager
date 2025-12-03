package com.raz.payment.infrastructure.database.model.mappers;

public interface EntityMapper<E, D> {
    E toEntity(D d);

    D toDomainModel(E e);
}
