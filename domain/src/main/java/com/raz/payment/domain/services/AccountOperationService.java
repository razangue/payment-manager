package com.raz.payment.domain.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.raz.payment.domain.exception.EntityNotFoundException;
import com.raz.payment.domain.exception.IncorrectAmountOperation;
import com.raz.payment.domain.exception.InsufficientFundsException;
import com.raz.payment.domain.interfaces.AccountOperationRepository;
import com.raz.payment.domain.interfaces.NotificationMessageSender;
import com.raz.payment.domain.model.account.Account;
import com.raz.payment.domain.model.client.OperationDetail;
import com.raz.payment.domain.model.client.OperationDetailType;
import com.raz.payment.domain.model.operation.AccountOperation;
import com.raz.payment.domain.model.operation.AccountOperationType;
import com.raz.payment.domain.model.operation.DepositOperation;
import com.raz.payment.domain.model.operation.PaymentOperation;
import com.raz.payment.domain.model.operation.WithdrawalOperation;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountOperationService {
  private final AccountService accountService;
  private final OperationDetailService operationDetailService;
  private final AccountOperationRepository accountOperationRepository;
  private final NotificationMessageSender notificationMessageSender;

  public AccountOperation save(AccountOperation accountOperation) {
    return accountOperationRepository.save(accountOperation);
  }

  @Transactional
  public AccountOperation makeDeposit(String sourceAccountNumber, BigDecimal amount) {
    if (!isAmountOK(amount)) {
      throw new IncorrectAmountOperation(amount);
    }
    Account sourceAccount = accountService.findByAccountNumber(sourceAccountNumber)
        .orElseThrow(() -> new EntityNotFoundException(Account.class, sourceAccountNumber));
    DepositOperation depositOperation = DepositOperation.builder()
        .sourceAccount(sourceAccount)
        .accountOperationType(AccountOperationType.DEPOSIT)
        .amount(amount)
        .date(LocalDateTime.now())
        .build();
    BigDecimal currentBalance = sourceAccount.getCurrentBalance();
    BigDecimal balanceAfterOperation = currentBalance.add(amount);
    return processInternalAccountOperation(sourceAccount, depositOperation,
        OperationDetailType.CREDIT, currentBalance, amount, balanceAfterOperation);
  }

   @Transactional
  public AccountOperation makeWithdrawal(String sourceAccountNumber, BigDecimal amount) {
    if (!isAmountOK(amount)) {
      throw new IncorrectAmountOperation(amount);
    }
    Account sourceAccount = accountService.findByAccountNumber(sourceAccountNumber)
        .orElseThrow(() -> new EntityNotFoundException(Account.class, sourceAccountNumber));
    if (isWithdrawalPossible(sourceAccount, amount)) {
      WithdrawalOperation withdrawalOperation = WithdrawalOperation.builder()
          .sourceAccount(sourceAccount)
          .accountOperationType(AccountOperationType.WITHDRAWAL)
          .amount(amount)
          .date(LocalDateTime.now())
          .build();
      BigDecimal currentBalance = sourceAccount.getCurrentBalance();
      BigDecimal balanceAfterOperation = currentBalance.subtract(amount);

      return processInternalAccountOperation(sourceAccount, withdrawalOperation,
          OperationDetailType.DEBIT, currentBalance, amount, balanceAfterOperation);
    } else {
      throw new InsufficientFundsException(sourceAccount);
    }
  }

   @Transactional
  public AccountOperation makePayment(String sourceAccountNumber, BigDecimal amount, String receivingAccountNumber) {
    if (!isAmountOK(amount)) {
      throw new IncorrectAmountOperation(amount);
    }
    Account sourceAccount = accountService.findByAccountNumber(sourceAccountNumber)
        .orElseThrow(() -> new EntityNotFoundException(Account.class, sourceAccountNumber));
    Account receivingAccount = accountService.findByAccountNumber(receivingAccountNumber)
        .orElseThrow(() -> new EntityNotFoundException(Account.class, receivingAccountNumber));

    if (isWithdrawalPossible(sourceAccount, amount)) {

      return processPaymentOperation(sourceAccount, receivingAccount, amount);
    } else {
      throw new InsufficientFundsException(sourceAccount);
    }
  }

  public boolean isAmountOK(BigDecimal amount) {
    return amount != null && amount.compareTo(BigDecimal.ZERO) >= 0;
  }

  public boolean isWithdrawalPossible(Account account, BigDecimal amount) {
    BigDecimal currentBalance = account.getCurrentBalance();
    return isAmountOK(amount) && currentBalance != null
        && currentBalance.compareTo(amount) >= 0;
  }

  @Transactional
  public AccountOperation processInternalAccountOperation(Account sourceAccount, AccountOperation accountOperation,
      OperationDetailType operationDetailType, BigDecimal currentBalance, BigDecimal amount,
      BigDecimal balanceAfterOperation) {
    AccountOperation saveOp = save(accountOperation);
    Account updatedAccount = accountService.updateAccountBalance(sourceAccount, balanceAfterOperation);
    OperationDetail operationDetail = operationDetailService.saveCreatedOperationDetail(updatedAccount,
        accountOperation, operationDetailType, currentBalance, amount, balanceAfterOperation);
    notificationMessageSender.sendNotificationOperations(Arrays.asList(operationDetail));
    return saveOp;
  }

  @Transactional
  public AccountOperation processPaymentOperation(Account sourceAccount, Account receivingAccount, BigDecimal amount) {
    PaymentOperation paymentOperation = PaymentOperation.builder()
        .sourceAccount(sourceAccount)
        .receivingAccount(receivingAccount)
        .accountOperationType(AccountOperationType.PAYMENT)
        .amount(amount)
        .date(LocalDateTime.now())
        .build();
    BigDecimal currentSourceAccountBalance = sourceAccount.getCurrentBalance();
    BigDecimal sourceAccountBalanceAfterOperation = currentSourceAccountBalance.subtract(amount);

    AccountOperation saveOp = save(paymentOperation);
    Account updatedSourceAccount = accountService.updateAccountBalance(sourceAccount,
        sourceAccountBalanceAfterOperation);

    OperationDetail debitOperation = operationDetailService.saveCreatedOperationDetail(
        updatedSourceAccount, saveOp,
        OperationDetailType.DEBIT, currentSourceAccountBalance, amount,
        sourceAccountBalanceAfterOperation);

    BigDecimal currentReceivingAccountBalance = receivingAccount.getCurrentBalance();
    BigDecimal receivingAccountBalanceAfterOperation = currentReceivingAccountBalance.add(amount);

    Account updatedReceivingAccount = accountService.updateAccountBalance(receivingAccount,
        receivingAccountBalanceAfterOperation);
    OperationDetail creditOperation = operationDetailService.saveCreatedOperationDetail(
        updatedReceivingAccount, saveOp,
        OperationDetailType.CREDIT, currentReceivingAccountBalance, amount,
        receivingAccountBalanceAfterOperation);
    notificationMessageSender.sendNotificationOperations(Arrays.asList(debitOperation, creditOperation));
    return paymentOperation;
  }
}
