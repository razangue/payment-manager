package com.raz.payment.infrastructure.database.model.mappers;

import org.springframework.stereotype.Component;

import com.raz.payment.domain.model.client.OperationDetail;
import com.raz.payment.infrastructure.database.model.entity.OperationDetailEntity;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OperationDetailMapper implements EntityMapper<OperationDetailEntity, OperationDetail> {

    private final AccountMapper accountMapper;
    private final AccountOperationMapper accountOperationMapper;

    @Override
    public OperationDetailEntity toEntity(OperationDetail opDetail) {
        return OperationDetailEntity.builder()
                .id(opDetail.getId())
                .account(accountMapper.toEntity(opDetail.getAccount()))
                .operation(accountOperationMapper.toEntity(opDetail.getOperation()))
                .accountBalanceBeforeOperation(opDetail.getAccountBalanceBeforeOperation())
                .amount(opDetail.getAmount())
                .accountBalanceAfterOperation(opDetail.getAccountBalanceAfterOperation())
                .operationDetailType(opDetail.getOperationDetailType())
                .date(opDetail.getDate())
                .build();
    }

    @Override
    public OperationDetail toDomainModel(OperationDetailEntity opDetailEntity) {
        return OperationDetail.builder()
                .id(opDetailEntity.getId())
                .account(accountMapper.toDomainModel(opDetailEntity.getAccount()))
                .operation(accountOperationMapper.toDomainModel(opDetailEntity.getOperation()))
                .accountBalanceBeforeOperation(opDetailEntity.getAccountBalanceBeforeOperation())
                .amount(opDetailEntity.getAmount())
                .accountBalanceAfterOperation(opDetailEntity.getAccountBalanceAfterOperation())
                .operationDetailType(opDetailEntity.getOperationDetailType())
                .date(opDetailEntity.getDate())
                .build();
    }

}
