package com.raz.payment.infrastructure.database.repository.impl;

import org.springframework.stereotype.Repository;

import com.raz.payment.domain.interfaces.OperationDetailRepository;
import com.raz.payment.domain.model.client.OperationDetail;
import com.raz.payment.infrastructure.database.model.entity.OperationDetailEntity;
import com.raz.payment.infrastructure.database.model.mappers.OperationDetailMapper;
import com.raz.payment.infrastructure.database.repository.interfaces.OperationDetailJpaRepository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class OperationDetailRepositoryImpl implements OperationDetailRepository {
    private final OperationDetailJpaRepository operationDetailJpaRepository;
    private final OperationDetailMapper operationDetailMapper;

    @Override
    public OperationDetail save(OperationDetail operationDetail) {
        OperationDetailEntity opDetailEntity = operationDetailMapper.toEntity(operationDetail);
        return operationDetailMapper.toDomainModel(operationDetailJpaRepository.save(opDetailEntity));
    }

}
