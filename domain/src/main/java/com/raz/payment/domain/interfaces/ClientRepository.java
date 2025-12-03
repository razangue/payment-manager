package com.raz.payment.domain.interfaces;

import java.util.Optional;
import java.util.UUID;

import com.raz.payment.domain.model.client.Client;

public interface ClientRepository {
    Client save(Client client);

    Optional<Client> findById(UUID id);
}
