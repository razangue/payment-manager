package com.raz.payment.domain.services;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.raz.payment.domain.interfaces.AccountRepository;
import com.raz.payment.domain.model.account.Account;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {

        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Optional<Account> findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public Account updateAccountBalance(Account accountToUpdate, BigDecimal balance) {
        accountToUpdate.setCurrentBalance(balance);
        return accountRepository.save(accountToUpdate);
    }
}
