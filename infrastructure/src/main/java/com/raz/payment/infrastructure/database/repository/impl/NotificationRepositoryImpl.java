package com.raz.payment.infrastructure.database.repository.impl;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.raz.payment.domain.interfaces.NotificationRepository;
import com.raz.payment.domain.model.client.Notification;
import com.raz.payment.infrastructure.database.model.entity.NotificationEntity;
import com.raz.payment.infrastructure.database.model.mappers.NotificationMapper;
import com.raz.payment.infrastructure.database.repository.interfaces.NotificationJpaRepository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {
    private final NotificationJpaRepository notificationJpaRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public Notification save(Notification notification) {
        NotificationEntity entity = notificationMapper.toEntity(notification);
        return notificationMapper.toDomainModel(notificationJpaRepository.save(entity));
    }

    @Override
    public Optional<Notification> findByOperationDetailId(Long operationDetailId) {
        return notificationJpaRepository.findByOperationDetailId(operationDetailId)
                .map(notificationMapper::toDomainModel);
    }

}
