package com.raz.payment.application.mappers;

import org.springframework.stereotype.Component;

import com.raz.payment.application.requests.CreateClientRequest;
import com.raz.payment.application.responses.ClientResponse;
import com.raz.payment.domain.model.client.Client;

@Component
public class ClientReqRespMapper implements DomainRequestResponseMapper<Client, CreateClientRequest, ClientResponse> {
    public ClientResponse domainToResponse(Client client) {
        return ClientResponse.builder()
                .id(client.getId())
                .lastName(client.getLastName())
                .firstName(client.getFirstName())
                .birthDate(client.getBirthDate())
                .gender(client.getGender())
                .nationality(client.getNationality())
                .build();
    }

    public Client responseToDomain(ClientResponse client) {
        return Client.builder()
                .id(client.getId())
                .lastName(client.getLastName())
                .firstName(client.getFirstName())
                .birthDate(client.getBirthDate())
                .gender(client.getGender())
                .nationality(client.getNationality())
                .build();
    }

    public Client requestToDomain(CreateClientRequest request) {
        return Client.builder()
                .lastName(request.getLastName())
                .firstName(request.getFirstName())
                .birthDate(request.getBirthDate())
                .gender(request.getGender())
                .nationality(request.getNationality())
                .build();
    }
}
