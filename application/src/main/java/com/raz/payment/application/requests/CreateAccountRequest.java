package com.raz.payment.application.requests;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raz.payment.application.responses.ClientResponse;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateAccountRequest(@NotNull String accountNumber, @NotEmpty List<ClientResponse> owners) {
@JsonCreator
    public CreateAccountRequest(
        @JsonProperty("accountNumber") String accountNumber,
        @JsonProperty("owners") List<ClientResponse> owners) {
        this.accountNumber = accountNumber;
        this.owners = owners;
    }
} 
