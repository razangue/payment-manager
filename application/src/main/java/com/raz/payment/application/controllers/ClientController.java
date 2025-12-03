package com.raz.payment.application.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raz.payment.application.mappers.ClientReqRespMapper;
import com.raz.payment.application.requests.CreateClientRequest;
import com.raz.payment.application.responses.ClientResponse;
import com.raz.payment.domain.model.client.Client;
import com.raz.payment.domain.services.ClientService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/clients")
@AllArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final ClientReqRespMapper clientReqRespMapper;

    @PostMapping("/create")
    @Operation(summary = "Create client", description = "Create a client")
    public ResponseEntity<ClientResponse> createClient(@RequestBody CreateClientRequest createClientRequest) {
        Client client = clientReqRespMapper.requestToDomain(createClientRequest);
        ClientResponse clientResponse = clientReqRespMapper.domainToResponse(clientService.create(client));
        return ResponseEntity.ok(clientResponse);
    }
}
