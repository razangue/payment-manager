package com.raz.payment.infrastructure.database.model.mappers;

import org.springframework.stereotype.Component;

import com.raz.payment.domain.model.client.Notification;
import com.raz.payment.infrastructure.database.model.entity.NotificationEntity;

@Component
public class NotificationMapper implements EntityMapper<NotificationEntity, Notification> {

    @Override
    public NotificationEntity toEntity(Notification notif) {
        return NotificationEntity.builder()
                .id(notif.getId())
                .accountNumber(notif.getAccountNumber())
                .operationDetailId(notif.getOperationDetailId())
                .accountBalanceBeforeOperation(notif.getAccountBalanceBeforeOperation())
                .amount(notif.getAmount())
                .accountBalanceAfterOperation(notif.getAccountBalanceAfterOperation())
                .operationDetailType(notif.getOperationDetailType())
                .date(notif.getDate())
                .build();
    }

    @Override
    public Notification toDomainModel(NotificationEntity notifEntity) {
        return Notification.builder()
                .id(notifEntity.getId())
                .accountNumber(notifEntity.getAccountNumber())
                .operationDetailId(notifEntity.getOperationDetailId())
                .accountBalanceBeforeOperation(notifEntity.getAccountBalanceBeforeOperation())
                .amount(notifEntity.getAmount())
                .accountBalanceAfterOperation(notifEntity.getAccountBalanceAfterOperation())
                .operationDetailType(notifEntity.getOperationDetailType())
                .date(notifEntity.getDate())
                .build();
    }
}
