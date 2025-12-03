package com.raz.payment.domain.interfaces;

import java.util.List;

import com.raz.payment.domain.model.client.OperationDetail;

public interface NotificationMessageSender {
    void sendNotificationOperations(List<OperationDetail> operationDetails);
}
