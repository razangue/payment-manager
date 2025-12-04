package com.raz.payment.infrastructure.database.model.mappers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.raz.payment.domain.model.account.Account;
import com.raz.payment.infrastructure.database.model.entity.AccountEntity;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AccountMapper implements EntityMapper<AccountEntity, Account> {
        private final AccountMapperWithoutOperationsBuilder accountMapperWithoutOperationsBuilder;
        private final AccountOperationMapper accountOperationMapper;

        public AccountEntity toEntity(Account account) {
                return accountMapperWithoutOperationsBuilder.toAccountEntityBuilder(account)
                                .operations(account.getOperations().stream()
                                                .map(accountOperationMapper::toEntity).toList())
                                .build();
        }

        public Account toDomainModel(AccountEntity accountEntity) {
                return accountMapperWithoutOperationsBuilder.toAccountBuilder(accountEntity)
                                .operations(accountEntity.getOperations().stream()
                                                .map(accountOperationMapper::toDomainModel).toList())
                                .build();
        }
}
