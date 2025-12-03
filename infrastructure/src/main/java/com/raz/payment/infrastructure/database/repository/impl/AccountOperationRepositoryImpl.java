package com.raz.payment.infrastructure.database.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.raz.payment.domain.interfaces.AccountOperationRepository;
import com.raz.payment.domain.model.operation.AccountOperation;
import com.raz.payment.infrastructure.database.model.entity.AccountOperationEntity;
import com.raz.payment.infrastructure.database.model.mappers.AccountOperationMapper;
import com.raz.payment.infrastructure.database.repository.interfaces.AccountOperationJpaRepository;

@Repository
public class AccountOperationRepositoryImpl implements AccountOperationRepository {

    private final AccountOperationJpaRepository accountOperationJpaRepository;
    private final AccountOperationMapper accountOperationMapper;

    public AccountOperationRepositoryImpl(AccountOperationJpaRepository accountOperationJpaRepository,
            AccountOperationMapper accountOperationMapper) {
        this.accountOperationJpaRepository = accountOperationJpaRepository;
        this.accountOperationMapper = accountOperationMapper;
    }

    @Override
    public AccountOperation save(AccountOperation accountOperation) {
        AccountOperationEntity entity = accountOperationMapper.toEntity(accountOperation);
        return accountOperationMapper.toDomainModel(accountOperationJpaRepository.save(entity));
    }

    @Override
    public List<AccountOperation> findAll() {
        return accountOperationJpaRepository.findAll().stream()
                .map(accountOperationMapper::toDomainModel)
                .toList();
    }

}
