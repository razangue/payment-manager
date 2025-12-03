package com.raz.payment.infrastructure.database.model.mappers;

import org.springframework.stereotype.Component;

import com.raz.payment.domain.model.account.Account.AccountBuilder;
import com.raz.payment.domain.model.operation.AccountOperation;
import com.raz.payment.domain.model.operation.AccountOperationType;
import com.raz.payment.domain.model.operation.DepositOperation;
import com.raz.payment.domain.model.operation.PaymentOperation;
import com.raz.payment.domain.model.operation.WithdrawalOperation;
import com.raz.payment.infrastructure.database.model.entity.AccountEntity.AccountEntityBuilder;
import com.raz.payment.infrastructure.database.model.entity.AccountOperationEntity;
import com.raz.payment.infrastructure.database.model.entity.AccountOperationEntity.AccountOperationEntityBuilder;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AccountOperationMapper implements EntityMapper<AccountOperationEntity, AccountOperation> {
    private final AccountMapperWithoutOperationsBuilder accountMapperWithoutOperationsBuilder;

    public AccountOperationEntity toEntity(AccountOperation accountOperation) {
        AccountEntityBuilder sourceAccountEntityBuilder = accountMapperWithoutOperationsBuilder
                .toAccountEntityBuilder(accountOperation.getSourceAccount());
        AccountOperationType accountOperationType = accountOperation.getAccountOperationType();
        AccountOperationEntityBuilder builder = AccountOperationEntity.builder()
                .id(accountOperation.getId())
                .sourceAccount(sourceAccountEntityBuilder.build())
                .operationType(accountOperationType)
                .amount(accountOperation.getAmount())
                .date(accountOperation.getDate());
        if (AccountOperationType.PAYMENT.equals(accountOperationType)) {
            PaymentOperation paymentOperation = (PaymentOperation) accountOperation;
            AccountEntityBuilder receivingAccountEntityBuilder = accountMapperWithoutOperationsBuilder
                    .toAccountEntityBuilder(paymentOperation.getReceivingAccount());
            builder.receivingAccount(receivingAccountEntityBuilder.build());
        }
        return builder.build();
    }

    public AccountOperation toDomainModel(AccountOperationEntity accountOperationEntity) {
        AccountBuilder sourceAccountBuilder = accountMapperWithoutOperationsBuilder
                .toAccountBuilder(accountOperationEntity.getSourceAccount());
        AccountOperationType accountOperationType = accountOperationEntity.getOperationType();
        return switch (accountOperationType) {
            case DEPOSIT -> DepositOperation.builder()
                    .id(accountOperationEntity.getId())
                    .sourceAccount(sourceAccountBuilder.build())
                    .accountOperationType(accountOperationType)
                    .amount(accountOperationEntity.getAmount())
                    .build();
            case WITHDRAWAL -> WithdrawalOperation.builder()
                    .id(accountOperationEntity.getId())
                    .sourceAccount(sourceAccountBuilder.build())
                    .accountOperationType(accountOperationType)
                    .amount(accountOperationEntity.getAmount())
                    .build();
            case PAYMENT -> PaymentOperation.builder()
                    .id(accountOperationEntity.getId())
                    .sourceAccount(sourceAccountBuilder.build())
                    .receivingAccount(accountMapperWithoutOperationsBuilder
                            .toAccountBuilder(accountOperationEntity.getReceivingAccount()).build())
                    .accountOperationType(accountOperationType)
                    .amount(accountOperationEntity.getAmount())
                    .build();
            default -> throw new IllegalArgumentException("Type d'opération inconnu: ");
        };
    }
}
