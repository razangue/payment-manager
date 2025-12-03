package com.raz.payment.domain.interfaces;

import java.util.List;

import com.raz.payment.domain.model.operation.AccountOperation;


public interface AccountOperationRepository {
    AccountOperation save(AccountOperation accountOperation);

    List<AccountOperation> findAll();
}
