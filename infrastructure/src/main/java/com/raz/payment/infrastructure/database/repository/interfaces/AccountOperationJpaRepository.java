package com.raz.payment.infrastructure.database.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raz.payment.infrastructure.database.model.entity.AccountOperationEntity;

public interface AccountOperationJpaRepository extends JpaRepository<AccountOperationEntity, Long> {

}