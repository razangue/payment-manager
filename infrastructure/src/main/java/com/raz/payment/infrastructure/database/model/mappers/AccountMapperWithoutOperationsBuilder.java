package com.raz.payment.infrastructure.database.model.mappers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.raz.payment.domain.model.account.Account;
import com.raz.payment.infrastructure.database.model.entity.AccountEntity;

@Component
public class AccountMapperWithoutOperationsBuilder {
    private final ClientMapper clientMapper;

    public AccountMapperWithoutOperationsBuilder(ClientMapper clientMapper) {
        this.clientMapper = clientMapper;
    }

    public AccountEntity.AccountEntityBuilder toAccountEntityBuilder(Account account) {
        return AccountEntity.builder()
                .accountNumber(account.getAccountNumber())
                .currentBalance(account.getCurrentBalance())
                .owners(account.getOwners().stream()
                        .map(clientMapper::toEntity).toList());
    }

    public Account.AccountBuilder toAccountBuilder(AccountEntity accountEntity) {
        return Account.builder()
                .id(Optional.ofNullable(accountEntity.getId()).map(UUID::toString).orElse(null))
                .accountNumber(accountEntity.getAccountNumber())
                .currentBalance(accountEntity.getCurrentBalance())
                .owners(accountEntity.getOwners().stream()
                        .map(clientMapper::toDomainModel).toList());
    }
}
