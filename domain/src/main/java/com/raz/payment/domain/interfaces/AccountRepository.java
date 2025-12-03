package com.raz.payment.domain.interfaces;

import java.util.Optional;

import com.raz.payment.domain.model.account.Account;

public interface AccountRepository {
     Account save(Account account);

     Optional<Account> findByAccountNumber(String accountNumber);
}
