package com.raz.payment.infrastructure.database.repository.interfaces;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.raz.payment.infrastructure.database.model.entity.AccountEntity;

import jakarta.persistence.LockModeType;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<AccountEntity> findByAccountNumber(String accountNumber);
}