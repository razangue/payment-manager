package com.raz.payment.infrastructure.kafka.consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.Acknowledgment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raz.payment.domain.model.client.Notification;
import com.raz.payment.domain.services.NotificationService;
import com.raz.payment.infrastructure.kafka.services.consumer.NotificationMessageConsumer;

class NotificationMessageConsumerTest {

    private ObjectMapper objectMapper;
    private NotificationService notificationService;
    private NotificationMessageConsumer consumer;
    private Acknowledgment ack;

    @BeforeEach
    void setup() {
        objectMapper = mock(ObjectMapper.class);
        notificationService = mock(NotificationService.class);
        consumer = new NotificationMessageConsumer(objectMapper, notificationService);
        ack = mock(Acknowledgment.class);
    }

    @Test
    void consumeNotification_shouldProcessNewNotification() throws Exception {
        
        String key = "42";
        String value = "{\"mock\":\"json\"}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("topic", 0, 0L, key, value);

        Notification notification = Notification.builder()
                .operationDetailId(42L)
                .accountNumber("ACC123")
                .amount(BigDecimal.valueOf(10))
                .accountBalanceBeforeOperation(BigDecimal.valueOf(100))
                .accountBalanceAfterOperation(BigDecimal.valueOf(110))
                .date(LocalDateTime.now())
                .build();

        // Mock ObjectMapper
        when(objectMapper.readValue(value, Notification.class)).thenReturn(notification);

        // Mock NotificationService → notification not processed
        when(notificationService.findByOperationDetailId(42L)).thenReturn(Optional.empty());

        consumer.consumeNotification(record, ack);

        verify(notificationService, times(1)).save(notification);
        verify(ack, times(1)).acknowledge();
    }

    @Test
    void consumeNotification_shouldSkipAlreadyProcessedNotification() throws Exception {
        String key = "42";
        String value = "{\"mock\":\"json\"}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("topic", 0, 0L, key, value);

        Notification notification = Notification.builder()
                .operationDetailId(42L)
                .accountNumber("ACC123")
                .build();

        when(objectMapper.readValue(value, Notification.class)).thenReturn(notification);
        // Notification already processed
        when(notificationService.findByOperationDetailId(42L)).thenReturn(Optional.of(notification));

        consumer.consumeNotification(record, ack);

        // don't call save method
        verify(notificationService, never()).save(any());
        verify(ack, times(1)).acknowledge();
    }

    @Test
    void consumeNotification_shouldHandleExceptionGracefully() throws Exception {
        String key = "42";
        String value = "{\"mock\":\"json\"}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("topic", 0, 0L, key, value);

        // ObjectMapper throws an exception
        when(objectMapper.readValue(value, Notification.class)).thenThrow(new RuntimeException("JSON error"));

        consumer.consumeNotification(record, ack);

        // ack can be ignored because exception → test no call to save
        verify(notificationService, never()).save(any());
        verify(ack, never()).acknowledge();
    }
}
