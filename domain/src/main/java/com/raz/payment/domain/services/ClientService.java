package com.raz.payment.domain.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.raz.payment.domain.interfaces.ClientRepository;
import com.raz.payment.domain.model.client.Client;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client create(Client client) {
        return clientRepository.save(client);
    }

    public Optional<Client> findById(String id) {
        return clientRepository.findById(UUID.fromString(id));
    }
}
