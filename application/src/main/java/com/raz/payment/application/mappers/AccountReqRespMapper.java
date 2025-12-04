package com.raz.payment.application.mappers;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.raz.payment.application.requests.CreateAccountRequest;
import com.raz.payment.application.responses.AccountResponse;
import com.raz.payment.domain.model.account.Account;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AccountReqRespMapper
        implements DomainRequestResponseMapper<Account, CreateAccountRequest, AccountResponse> {
    private final ClientReqRespMapper clientReqRespMapper;

    public AccountResponse domainToResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .currentBalance(account.getCurrentBalance())
                .owners(account.getOwners().stream().map(clientReqRespMapper::domainToResponse).collect(Collectors.toCollection(ArrayList::new)))
                // .operations()
                .build();
    }

    public Account responseToDomain(AccountResponse account) {
        return Account.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .currentBalance(account.getCurrentBalance())
                .owners(account.getOwners().stream().map(clientReqRespMapper::responseToDomain).collect(Collectors.toCollection(ArrayList::new)))
                // .operations()
                .build();
    }

    public Account requestToDomain(CreateAccountRequest createAccountRequest) {
        return Account.builder()
                .accountNumber(createAccountRequest.getAccountNumber())
                .owners(createAccountRequest.getOwners().stream().map(clientReqRespMapper::responseToDomain).collect(Collectors.toCollection(ArrayList::new)))
                .build();
    }
}