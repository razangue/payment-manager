package com.raz.payment.application.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raz.payment.application.requests.InternalOperationRequest;
import com.raz.payment.application.requests.PaymentOperationRequest;
import com.raz.payment.domain.services.AccountOperationService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/operations")
@AllArgsConstructor
public class AccountOperationController {
    private final AccountOperationService accountOperationService;

    @PostMapping("/deposit")
    @Operation(summary = "Make a deposit", description = "Make a deposit in an account")
    public ResponseEntity<String> makeDeposit(@RequestBody InternalOperationRequest operationRequest) {
        accountOperationService.makeDeposit(operationRequest.accountNumber(), operationRequest.amount());
        return ResponseEntity.ok("Deposit have been successfully done");
    }

    @PostMapping("/withdrawal")
    @Operation(summary = "Make a withdrawal", description = "Make a withdrawal in an account")
    public ResponseEntity<String> makeWithdrawal(@RequestBody InternalOperationRequest operationRequest) {
        accountOperationService.makeWithdrawal(operationRequest.accountNumber(), operationRequest.amount());
        return ResponseEntity.ok("withdrawal have been successfully done");
    }

    @PostMapping("/payment")
    @Operation(summary = "Make a payment", description = "Make a payment from an account to another account")
    public ResponseEntity<String> makePayment(@RequestBody PaymentOperationRequest paymentRequest) {
        accountOperationService.makePayment(paymentRequest.sourceAccountNumber(), paymentRequest.amount(),
                paymentRequest.receivingAccountNumber());
        return ResponseEntity.ok("Payment have been successfully done");
    }
}
