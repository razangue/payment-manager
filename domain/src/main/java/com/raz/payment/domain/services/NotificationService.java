package com.raz.payment.domain.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.raz.payment.domain.interfaces.NotificationRepository;
import com.raz.payment.domain.model.client.Notification;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Transactional
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    public Optional<Notification> findByOperationDetailId(Long operationDetailId) {
        return notificationRepository.findByOperationDetailId(operationDetailId);
    }
}
