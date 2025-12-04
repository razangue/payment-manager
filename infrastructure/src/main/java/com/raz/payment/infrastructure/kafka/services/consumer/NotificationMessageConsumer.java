package com.raz.payment.infrastructure.kafka.services.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raz.payment.domain.model.client.Notification;
import com.raz.payment.domain.services.NotificationService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class NotificationMessageConsumer {
   private static final Logger logger = LoggerFactory.getLogger(NotificationMessageConsumer.class);
   private final ObjectMapper customObjectMapper;
   private final NotificationService notificationService;

   @KafkaListener(topics = "${operation.notification.kafka.topic.name}", 
   groupId = "${spring.kafka.consumer.group-id}",
containerFactory = "kafkaListenerContainerFactory"
)
   public void consumeNotification(ConsumerRecord<String, String> record,
                                                     Acknowledgment ack) {
        String key = record.key();
        String value = record.value();

        try {
            Notification  notification  = customObjectMapper.readValue(value, Notification.class);
            // Avoid doublons
            if (notificationService.findByOperationDetailId(notification .getOperationDetailId()).isPresent()) {
                logger.info("Notification {} already processed, skipping.", notification .getOperationDetailId());
            } else {
                notificationService.save(notification);
                logger.info("Processed notification for operationDetail {} and account number {}", 
                            notification.getOperationDetailId(),
                            notification.getAccountNumber());
            }
            // manual ACK after processing
            ack.acknowledge();

        } catch (Exception e) {
            logger.error("Failed to process notification with key {}: {}", key, e.getMessage(), e);
        }
    }
}
