package com.raz.payment.domain.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.raz.payment.domain.interfaces.OperationDetailRepository;
import com.raz.payment.domain.model.account.Account;
import com.raz.payment.domain.model.client.OperationDetail;
import com.raz.payment.domain.model.client.OperationDetailType;
import com.raz.payment.domain.model.operation.AccountOperation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OperationDetailService {
    private final OperationDetailRepository operationDetailRepository;

    @Transactional
    public OperationDetail save(OperationDetail operationDetail) {
        return operationDetailRepository.save(operationDetail);
    }

    public OperationDetail createOperationDetail(Account account, AccountOperation operation,
            OperationDetailType operationDetailType, BigDecimal accountBalanceBeforeOperation, BigDecimal amount,
            BigDecimal accountBalanceAfterOperation) {
        return OperationDetail.builder()
                .account(account)
                .operation(operation)
                .operationDetailType(operationDetailType)
                .accountBalanceBeforeOperation(accountBalanceBeforeOperation)
                .amount(amount)
                .accountBalanceAfterOperation(accountBalanceAfterOperation)
                .date(operation.getDate())
                .build();
    }

    public OperationDetail saveCreatedOperationDetail(Account account, AccountOperation operation,
            OperationDetailType operationDetailType, BigDecimal accountBalanceBeforeOperation, BigDecimal amount,
            BigDecimal accountBalanceAfterOperation) {
        return save(createOperationDetail(account, operation, operationDetailType, accountBalanceBeforeOperation, amount, accountBalanceAfterOperation));
    }
}
