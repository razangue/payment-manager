package com.raz.payment.infrastructure.database.repository.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.raz.payment.domain.interfaces.ClientRepository;
import com.raz.payment.domain.model.client.Client;
import com.raz.payment.infrastructure.database.model.entity.ClientEntity;
import com.raz.payment.infrastructure.database.model.mappers.ClientMapper;
import com.raz.payment.infrastructure.database.repository.interfaces.ClientJpaRepository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class ClientRepositoryImpl implements ClientRepository {
    private final ClientMapper clientMapper;
    private final ClientJpaRepository clientJpaRepository;

    @Override
    public Client save(Client client) {
        ClientEntity entity = clientMapper.toEntity(client);
        return clientMapper.toDomainModel(clientJpaRepository.save(entity));
    }

    @Override
    public Optional<Client> findById(UUID id) {
        return clientJpaRepository.findById(id)
                .map(clientMapper::toDomainModel);
    }

}
