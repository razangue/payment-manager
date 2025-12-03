package com.raz.payment.infrastructure.database.repository.interfaces;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raz.payment.infrastructure.database.model.entity.NotificationEntity;

public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {
    Optional<NotificationEntity> findByOperationDetailId(Long operationDetailId);
}
