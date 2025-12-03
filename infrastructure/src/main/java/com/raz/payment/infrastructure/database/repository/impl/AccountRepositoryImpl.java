package com.raz.payment.infrastructure.database.repository.impl;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.raz.payment.domain.interfaces.AccountRepository;
import com.raz.payment.domain.model.account.Account;
import com.raz.payment.infrastructure.database.model.entity.AccountEntity;
import com.raz.payment.infrastructure.database.model.mappers.AccountMapper;
import com.raz.payment.infrastructure.database.repository.interfaces.AccountJpaRepository;

@Repository
public class AccountRepositoryImpl implements AccountRepository {
        private final AccountMapper accountMapper;
        private final AccountJpaRepository accountJpaRepository;

        public AccountRepositoryImpl(AccountMapper accountMapper, AccountJpaRepository accountJpaRepository) {
                this.accountMapper = accountMapper;
                this.accountJpaRepository = accountJpaRepository;
        }

        @Override
        public Account save(Account account) {
                AccountEntity entity = accountMapper.toEntity(account);
                return accountMapper.toDomainModel(accountJpaRepository.save(entity));
        }

        @Override
        public Optional<Account> findByAccountNumber(String accountNumber) {
                return accountJpaRepository.findByAccountNumber(accountNumber)
                                .map(accountMapper::toDomainModel);
        }
}
