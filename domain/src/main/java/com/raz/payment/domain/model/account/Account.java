package com.raz.payment.domain.model.account;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.raz.payment.domain.model.client.Client;
import com.raz.payment.domain.model.operation.AccountOperation;

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
public class Account {
    private String id;
    private String accountNumber;
    @Builder.Default
    private BigDecimal currentBalance = BigDecimal.ZERO;
    @Builder.Default
    private List<Client> owners = new ArrayList<>();
    @Builder.Default
    private List<AccountOperation> operations= new ArrayList<>();
}
