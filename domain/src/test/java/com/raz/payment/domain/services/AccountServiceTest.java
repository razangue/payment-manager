package com.raz.payment.domain.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import com.raz.payment.domain.interfaces.AccountRepository;
import com.raz.payment.domain.model.account.Account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class AccountServiceTest {

    private AccountRepository accountRepository;
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        accountService = new AccountService(accountRepository);
    }

    private Account createAccount(String number, BigDecimal balance) {
        return Account.builder()
                .id("id-" + number)
                .accountNumber(number)
                .currentBalance(balance)
                .build();
    }

    // ---------------------------
    // CREATE ACCOUNT
    // ---------------------------

    @Test
    void createAccount_shouldSaveAndReturnAccount() {
        Account acc = createAccount("ACC1", BigDecimal.valueOf(100));

        when(accountRepository.save(acc)).thenReturn(acc);

        Account result = accountService.createAccount(acc);

        assertEquals(acc, result);
        verify(accountRepository).save(acc);
    }

    // ---------------------------
    // FIND BY ACCOUNT NUMBER
    // ---------------------------

    @Test
    void findByAccountNumber_shouldReturnAccount() {
        Account acc = createAccount("ACC2", BigDecimal.valueOf(200));

        when(accountRepository.findByAccountNumber("ACC2")).thenReturn(Optional.of(acc));

        Optional<Account> result = accountService.findByAccountNumber("ACC2");

        assertTrue(result.isPresent());
        assertEquals(acc, result.get());
    }

    @Test
    void findByAccountNumber_shouldReturnEmptyWhenNotFound() {
        when(accountRepository.findByAccountNumber("UNKNOWN")).thenReturn(Optional.empty());

        Optional<Account> result = accountService.findByAccountNumber("UNKNOWN");

        assertTrue(result.isEmpty());
    }

    // ---------------------------
    // UPDATE BALANCE
    // ---------------------------

    @Test
    void updateAccountBalance_shouldUpdateAndSaveAccount() {
        Account acc = createAccount("ACC3", BigDecimal.valueOf(300));

        when(accountRepository.save(acc)).thenReturn(acc);

        Account updated = accountService.updateAccountBalance(acc, BigDecimal.valueOf(350));

        assertEquals(BigDecimal.valueOf(350), updated.getCurrentBalance());

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());

        assertEquals(BigDecimal.valueOf(350), captor.getValue().getCurrentBalance());
    }
}
