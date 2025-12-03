package com.raz.payment.infrastructure.database.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raz.payment.infrastructure.database.model.entity.OperationDetailEntity;

public interface OperationDetailJpaRepository extends JpaRepository<OperationDetailEntity, Long> {

}
