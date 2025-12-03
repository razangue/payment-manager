package com.raz.payment.infrastructure.database.repository.interfaces;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raz.payment.infrastructure.database.model.entity.ClientEntity;

public interface ClientJpaRepository extends JpaRepository<ClientEntity, UUID> {

}
