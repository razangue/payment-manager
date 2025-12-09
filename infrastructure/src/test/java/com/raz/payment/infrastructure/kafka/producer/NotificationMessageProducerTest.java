package com.raz.payment.infrastructure.kafka.producer;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raz.payment.domain.model.account.Account;
import com.raz.payment.domain.model.client.Notification;
import com.raz.payment.domain.model.client.OperationDetail;
import com.raz.payment.domain.model.client.OperationDetailType;
import com.raz.payment.infrastructure.kafka.services.producer.NotificationMessageProducer;

class NotificationMessageProducerTest {

    private KafkaTemplate<String, String> kafkaTemplate;
    private ObjectMapper objectMapper;
    private NotificationMessageProducer producer;

    @BeforeEach
    void setup() {
        kafkaTemplate = mock(KafkaTemplate.class);
        objectMapper = mock(ObjectMapper.class);

        producer = new NotificationMessageProducer(kafkaTemplate, "test-topic", objectMapper);
    }

    @Test
    void sendNotificationOperations_shouldSendJsonMessageToKafka() throws Exception {

        OperationDetail detail = OperationDetail.builder()
                .id(42L)
                .account(Account.builder().accountNumber("ACC123").build())
                .operationDetailType(OperationDetailType.DEBIT)
                .amount(BigDecimal.TEN)
                .accountBalanceBeforeOperation(BigDecimal.valueOf(100))
                .accountBalanceAfterOperation(BigDecimal.valueOf(90))
                .date(LocalDateTime.now())
                .build();

        when(objectMapper.writeValueAsString(any(Notification.class)))
                .thenReturn("{\"json\":\"ok\"}");

        when(kafkaTemplate.send(anyString(), anyString(), anyString()))
                .thenReturn(CompletableFuture.completedFuture(null));

        producer.sendNotificationOperations(List.of(detail));

        // ObjectMapper verification
        verify(objectMapper, times(1))
                .writeValueAsString(any(Notification.class));

        // Kafka send verification
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);

        verify(kafkaTemplate, times(1))
                .send(eq("test-topic"), keyCaptor.capture(), jsonCaptor.capture());

        assert keyCaptor.getValue().equals("42");
        assert jsonCaptor.getValue().equals("{\"json\":\"ok\"}");
    }

    @Test
    void fromOperationDetailToNotificationTest() {

        Account account = Account.builder()
                .accountNumber("ACC555")
                .build();

        OperationDetail detail = OperationDetail.builder()
                .id(9L)
                .account(account)
                .amount(BigDecimal.TEN)
                .accountBalanceBeforeOperation(BigDecimal.valueOf(50))
                .accountBalanceAfterOperation(BigDecimal.valueOf(40))
                .operationDetailType(OperationDetailType.DEBIT)
                .date(LocalDateTime.of(2024, 1, 1, 12, 0))
                .build();

        Notification notif = producer.fromOperationDetail(detail);

        assert notif.getAccountNumber().equals("ACC555");
        assert notif.getOperationDetailId().equals(9L);
        assert notif.getAmount().equals(BigDecimal.TEN);
        assert notif.getOperationDetailType().equals(OperationDetailType.DEBIT);
        assert notif.getDate().equals(LocalDateTime.of(2024, 1, 1, 12, 0));
        assert notif.getAccountBalanceBeforeOperation().equals(BigDecimal.valueOf(50));
        assert notif.getAccountBalanceAfterOperation().equals(BigDecimal.valueOf(40));
    }
}
