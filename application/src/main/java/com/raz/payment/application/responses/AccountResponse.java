package com.raz.payment.application.responses;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountResponse {
    private String id;
    private String accountNumber;
    private BigDecimal currentBalance;
    private List<ClientResponse> owners;
    private List<AccountOperationResponse> operations;
}
