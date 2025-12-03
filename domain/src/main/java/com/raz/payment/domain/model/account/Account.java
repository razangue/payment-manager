package com.raz.payment.domain.model.account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.raz.payment.domain.model.client.Client;
import com.raz.payment.domain.model.operation.AccountOperation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Account {
    private String id;
    private String accountNumber;
    private BigDecimal currentBalance;
    private List<Client> owners;
    private List<AccountOperation> operations;
}
