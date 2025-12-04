package com.raz.payment.infrastructure.database.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.raz.payment.domain.model.client.OperationDetailType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notification")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;

    @Column(unique = true, nullable = false)
    private Long operationDetailId;
    
    private BigDecimal accountBalanceBeforeOperation;
    private BigDecimal amount;
    private BigDecimal accountBalanceAfterOperation;
    @Enumerated(EnumType.STRING)
    private OperationDetailType operationDetailType;
    private LocalDateTime date;
}
