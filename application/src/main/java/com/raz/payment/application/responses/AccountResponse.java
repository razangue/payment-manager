package com.raz.payment.application.responses;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    @Builder.Default
    private BigDecimal currentBalance=BigDecimal.ZERO;
    @Builder.Default
    private List<ClientResponse> owners = new ArrayList<>();
    @Builder.Default
    private List<AccountOperationResponse> operations = new ArrayList<>();
}
