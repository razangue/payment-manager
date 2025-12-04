package com.raz.payment.infrastructure.kafka.services.producer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raz.payment.domain.interfaces.NotificationMessageSender;
import com.raz.payment.domain.model.client.Notification;
import com.raz.payment.domain.model.client.OperationDetail;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationMessageProducer implements NotificationMessageSender {
    private static final Logger logger = LoggerFactory.getLogger(NotificationMessageProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("${operation.notification.kafka.topic.name}")
    private String topic;
    private final ObjectMapper customObjectMapper;

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendNotificationOperations(List<OperationDetail> operationDetails) {
        operationDetails.stream().map(this::fromOperationDetail).forEach(notif -> {
            try {
                // Serialize to JSON
                String json = customObjectMapper.writeValueAsString(notif);

                // Business key for ordering and idempotence → operationDetailId
                String messageKey = notif.getOperationDetailId().toString();

                kafkaTemplate.send(topic, messageKey, json)
                        .whenComplete((result, ex) -> {
                            if (ex == null) {
                                logger.info(
                                        "Notification {} sent to topic {}, partition {}, offset {}",
                                        notif.getOperationDetailId(),
                                        result.getRecordMetadata().topic(),
                                        result.getRecordMetadata().partition(),
                                        result.getRecordMetadata().offset());
                            } else {
                                logger.error("Failed to send notification {}", notif.getOperationDetailId(), ex);
                            }
                        });

            } catch (Exception e) {
                logger.error("Serialization error for notification {}", notif.getOperationDetailId(), e);
            }
        });
    }

    public Notification fromOperationDetail(OperationDetail operationDetail) {
        return Notification.builder()
                .accountNumber(operationDetail.getAccount().getAccountNumber())
                .operationDetailId(operationDetail.getId())
                .accountBalanceBeforeOperation(operationDetail.getAccountBalanceBeforeOperation())
                .amount(operationDetail.getAmount())
                .accountBalanceAfterOperation(operationDetail.getAccountBalanceAfterOperation())
                .operationDetailType(operationDetail.getOperationDetailType())
                .date(operationDetail.getDate())
                .build();
    }
}
