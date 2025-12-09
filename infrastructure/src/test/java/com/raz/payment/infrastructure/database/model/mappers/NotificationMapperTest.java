package com.raz.payment.infrastructure.database.model.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.raz.payment.domain.model.client.Notification;
import com.raz.payment.domain.model.client.OperationDetailType;
import com.raz.payment.infrastructure.database.model.entity.NotificationEntity;

class NotificationMapperTest {
    
    private NotificationMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new NotificationMapper();
    }

    @Test
    void toEntity_shouldMapAllFieldsCorrectly() {
     
        Notification notif = Notification.builder()
                .id(1L)
                .accountNumber("ACC123")
                .operationDetailId(99L)
                .accountBalanceBeforeOperation(new BigDecimal(1000.00))
                .amount(new BigDecimal(250.00))
                .accountBalanceAfterOperation(new BigDecimal(750.00))
                .operationDetailType(OperationDetailType.DEBIT)
                .date(LocalDateTime.of(2024, 1, 10, 12, 30))
                .build();

        
        NotificationEntity entity = mapper.toEntity(notif);

        assertEquals(1L, entity.getId());
        assertEquals("ACC123", entity.getAccountNumber());
        assertEquals(99L, entity.getOperationDetailId());
        assertEquals(new BigDecimal(1000.00), entity.getAccountBalanceBeforeOperation());
        assertEquals(new BigDecimal(250.00), entity.getAmount());
        assertEquals(new BigDecimal(750.00), entity.getAccountBalanceAfterOperation());
        assertEquals(OperationDetailType.DEBIT, entity.getOperationDetailType());
        assertEquals(LocalDateTime.of(2024, 1, 10, 12, 30), entity.getDate());
    }

    @Test
    void toEntity_shouldHandleNullFields() {
        
        Notification notif = Notification.builder().build();

        NotificationEntity entity = mapper.toEntity(notif);

        assertNull(entity.getId());
        assertNull(entity.getAccountNumber());
        assertNull(entity.getOperationDetailId());
        assertNull(entity.getAccountBalanceBeforeOperation());
        assertNull(entity.getAmount());
        assertNull(entity.getAccountBalanceAfterOperation());
        assertNull(entity.getOperationDetailType());
        assertNull(entity.getDate());
    }

    @Test
    void toDomainModel_shouldMapAllFieldsCorrectly() {
    
        NotificationEntity entity = NotificationEntity.builder()
                .id(2L)
                .accountNumber("ACC999")
                .operationDetailId(123L)
                .accountBalanceBeforeOperation(new BigDecimal(500.00))
                .amount(new BigDecimal(100.00))
                .accountBalanceAfterOperation(new BigDecimal(400.00))
                .operationDetailType(OperationDetailType.CREDIT)
                .date(LocalDateTime.of(2024, 2, 5, 9, 45))
                .build();

        Notification domain = mapper.toDomainModel(entity);

        assertEquals(2L, domain.getId());
        assertEquals("ACC999", domain.getAccountNumber());
        assertEquals(123L, domain.getOperationDetailId());
        assertEquals(new BigDecimal(500.00), domain.getAccountBalanceBeforeOperation());
        assertEquals(new BigDecimal(100.00), domain.getAmount());
        assertEquals(new BigDecimal(400.00), domain.getAccountBalanceAfterOperation());
        assertEquals(OperationDetailType.CREDIT, domain.getOperationDetailType());
        assertEquals(LocalDateTime.of(2024, 2, 5, 9, 45), domain.getDate());
    }

    @Test
    void toDomainModel_shouldHandleNullFields() {
        NotificationEntity entity = NotificationEntity.builder().build();

        Notification domain = mapper.toDomainModel(entity);

        assertNull(domain.getId());
        assertNull(domain.getAccountNumber());
        assertNull(domain.getOperationDetailId());
        assertNull(domain.getAccountBalanceBeforeOperation());
        assertNull(domain.getAmount());
        assertNull(domain.getAccountBalanceAfterOperation());
        assertNull(domain.getOperationDetailType());
        assertNull(domain.getDate());
    }
}
