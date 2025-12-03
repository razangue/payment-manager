package com.raz.payment.domain.interfaces;

import java.util.Optional;

import com.raz.payment.domain.model.client.Notification;

public interface NotificationRepository {
    Notification save (Notification notification);
    Optional<Notification> findByOperationDetailId(Long operationDetailId);
}
