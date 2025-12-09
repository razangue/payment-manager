package com.raz.payment.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.raz.payment.domain.exception.EntityNotFoundException;
import com.raz.payment.domain.exception.IncorrectAmountOperation;
import com.raz.payment.domain.exception.InsufficientFundsException;
import com.raz.payment.domain.interfaces.AccountOperationRepository;
import com.raz.payment.domain.interfaces.NotificationMessageSender;
import com.raz.payment.domain.model.account.Account;
import com.raz.payment.domain.model.client.OperationDetail;
import com.raz.payment.domain.model.operation.AccountOperation;
import com.raz.payment.domain.model.operation.AccountOperationType;
import com.raz.payment.domain.model.operation.DepositOperation;
import com.raz.payment.domain.model.operation.PaymentOperation;
import com.raz.payment.domain.model.operation.WithdrawalOperation;

class AccountOperationServiceTest {

    private AccountService accountService;
    private OperationDetailService operationDetailService;
    private AccountOperationRepository accountOperationRepository;
    private NotificationMessageSender notificationMessageSender;

    private AccountOperationService service;

    @BeforeEach
    void setUp() {
        accountService = mock(AccountService.class);
        operationDetailService = mock(OperationDetailService.class);
        accountOperationRepository = mock(AccountOperationRepository.class);
        notificationMessageSender = mock(NotificationMessageSender.class);

        service = new AccountOperationService(accountService, operationDetailService,
                accountOperationRepository, notificationMessageSender);
    }

    private Account createAccount(String number, BigDecimal balance) {
        return Account.builder()
                .id("id-" + number)
                .accountNumber(number)
                .currentBalance(balance)
                .build();
    }

    // -------------------- DEPOSIT --------------------
    @Test
    void makeDeposit_shouldReturnDepositOperation() {
        Account account = createAccount("ACC1", BigDecimal.valueOf(100));
        when(accountService.findByAccountNumber("ACC1")).thenReturn(Optional.of(account));
        when(accountService.updateAccountBalance(account, BigDecimal.valueOf(200))).thenReturn(account);
        when(accountOperationRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(operationDetailService.saveCreatedOperationDetail(any(), any(), any(), any(), any(), any()))
                .thenReturn(mock(OperationDetail.class));

        AccountOperation result = service.makeDeposit("ACC1", BigDecimal.valueOf(100));

        assertTrue(result instanceof DepositOperation);
        assertEquals(AccountOperationType.DEPOSIT, result.getAccountOperationType());
        verify(accountOperationRepository).save(result);
        verify(notificationMessageSender).sendNotificationOperations(any());
    }

    @Test
    void makeDeposit_shouldThrowOnIncorrectAmount() {
        assertThrows(IncorrectAmountOperation.class, () -> service.makeDeposit("ACC1", BigDecimal.valueOf(-10)));
    }

    @Test
    void makeDeposit_shouldThrowOnAccountNotFound() {
        when(accountService.findByAccountNumber("ACC1")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.makeDeposit("ACC1", BigDecimal.valueOf(10)));
    }

    // -------------------- WITHDRAWAL --------------------
    @Test
    void makeWithdrawal_shouldReturnWithdrawalOperation() {
        Account account = createAccount("ACC2", BigDecimal.valueOf(200));
        when(accountService.findByAccountNumber("ACC2")).thenReturn(Optional.of(account));
        when(accountService.updateAccountBalance(account, BigDecimal.valueOf(150))).thenReturn(account);
        when(accountOperationRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(operationDetailService.saveCreatedOperationDetail(any(), any(), any(), any(), any(), any()))
                .thenReturn(mock(OperationDetail.class));

        AccountOperation result = service.makeWithdrawal("ACC2", BigDecimal.valueOf(50));

        assertTrue(result instanceof WithdrawalOperation);
        assertEquals(AccountOperationType.WITHDRAWAL, result.getAccountOperationType());
        verify(accountOperationRepository).save(result);
    }

    @Test
    void makeWithdrawal_shouldThrowInsufficientFunds() {
        Account account = createAccount("ACC3", BigDecimal.valueOf(30));
        when(accountService.findByAccountNumber("ACC3")).thenReturn(Optional.of(account));

        assertThrows(InsufficientFundsException.class, () -> service.makeWithdrawal("ACC3", BigDecimal.valueOf(50)));
    }

    // -------------------- PAYMENT --------------------
    @Test
    void makePayment_shouldReturnPaymentOperation() {
        Account source = createAccount("SRC", BigDecimal.valueOf(500));
        Account receiver = createAccount("RCV", BigDecimal.valueOf(100));

        when(accountService.findByAccountNumber("SRC")).thenReturn(Optional.of(source));
        when(accountService.findByAccountNumber("RCV")).thenReturn(Optional.of(receiver));

        when(accountService.updateAccountBalance(eq(source), any())).thenReturn(source);
        when(accountService.updateAccountBalance(eq(receiver), any())).thenReturn(receiver);

        when(accountOperationRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(operationDetailService.saveCreatedOperationDetail(any(), any(), any(), any(), any(), any()))
                .thenReturn(mock(OperationDetail.class));

        AccountOperation result = service.makePayment("SRC", BigDecimal.valueOf(50), "RCV");

        assertTrue(result instanceof PaymentOperation);
        assertEquals(AccountOperationType.PAYMENT, result.getAccountOperationType());

        // vérifier que notification est envoyée pour deux operations
        ArgumentCaptor<java.util.List> captor = ArgumentCaptor.forClass(java.util.List.class);
        verify(notificationMessageSender).sendNotificationOperations(captor.capture());
        assertEquals(2, captor.getValue().size());
    }

    @Test
    void makePayment_shouldThrowInsufficientFunds() {
        Account source = createAccount("SRC", BigDecimal.valueOf(10));
        Account receiver = createAccount("RCV", BigDecimal.valueOf(100));

        when(accountService.findByAccountNumber("SRC")).thenReturn(Optional.of(source));
        when(accountService.findByAccountNumber("RCV")).thenReturn(Optional.of(receiver));

        assertThrows(InsufficientFundsException.class,
                () -> service.makePayment("SRC", BigDecimal.valueOf(50), "RCV"));
    }
}
