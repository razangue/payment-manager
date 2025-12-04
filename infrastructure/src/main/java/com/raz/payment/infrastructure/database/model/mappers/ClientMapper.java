package com.raz.payment.infrastructure.database.model.mappers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.raz.payment.domain.model.client.Client;
import com.raz.payment.infrastructure.database.model.entity.ClientEntity;

@Component
public class ClientMapper implements EntityMapper<ClientEntity, Client> {
    public ClientEntity toEntity(Client client) {
        return ClientEntity.builder()
                .id(Optional.ofNullable(client.getId()).map(UUID::fromString).orElse(null) )
                .lastName(client.getLastName())
                .firstName(client.getFirstName())
                .birthDate(client.getBirthDate())
                .gender(client.getGender())
                .nationality(client.getNationality())
                .build();
    }

    public Client toDomainModel(ClientEntity client) {
        return Client.builder()
                .id(Optional.ofNullable(client.getId()).map(UUID::toString).orElse(null))
                .lastName(client.getLastName())
                .firstName(client.getFirstName())
                .birthDate(client.getBirthDate())
                .gender(client.getGender())
                .nationality(client.getNationality())
                .build();
    }

}
