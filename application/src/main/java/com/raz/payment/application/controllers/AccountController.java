package com.raz.payment.application.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raz.payment.application.mappers.AccountReqRespMapper;
import com.raz.payment.application.requests.CreateAccountRequest;
import com.raz.payment.application.responses.AccountResponse;
import com.raz.payment.domain.model.account.Account;
import com.raz.payment.domain.services.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final AccountReqRespMapper accountReqRespMapper;

    @PostMapping("/create")
    @Operation(summary = "Create account", description = "Create an account")
    public ResponseEntity<AccountResponse> createAccount(@RequestBody CreateAccountRequest createAccountRequest) {

        Account account = accountReqRespMapper.requestToDomain(createAccountRequest);
        AccountResponse accountResponse = accountReqRespMapper.domainToResponse(accountService.createAccount(account));
        return ResponseEntity.ok(accountResponse);
    }
}
