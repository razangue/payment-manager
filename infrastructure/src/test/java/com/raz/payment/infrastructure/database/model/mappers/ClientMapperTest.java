package com.raz.payment.infrastructure.database.model.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.raz.payment.domain.model.client.Client;
import com.raz.payment.infrastructure.database.model.entity.ClientEntity;

class ClientMapperTest {
    private ClientMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ClientMapper();
    }

    @Test
    void toEntity_shouldMapAllFieldsCorrectly() {
        String idString = UUID.randomUUID().toString();
        Client client = Client.builder()
                .id(idString)
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .gender("M")
                .nationality("FR")
                .build();

        ClientEntity entity = mapper.toEntity(client);

        assertEquals(UUID.fromString(idString), entity.getId());
        assertEquals("John", entity.getFirstName());
        assertEquals("Doe", entity.getLastName());
        assertEquals(LocalDate.of(1990, 1, 1), entity.getBirthDate());
        assertEquals("M", entity.getGender());
        assertEquals("FR", entity.getNationality());
    }

    @Test
    void toEntity_shouldHandleNullId() {
        Client client = Client.builder()
                .id(null)
                .firstName("Jane")
                .lastName("Smith")
                .build();

        ClientEntity entity = mapper.toEntity(client);

        assertNull(entity.getId());
        assertEquals("Jane", entity.getFirstName());
        assertEquals("Smith", entity.getLastName());
    }

    @Test
    void toDomainModel_shouldMapAllFieldsCorrectly() {

        UUID id = UUID.randomUUID();
        ClientEntity entity = ClientEntity.builder()
                .id(id)
                .firstName("Alice")
                .lastName("Johnson")
                .birthDate(LocalDate.of(1985, 5, 20))
                .gender("F")
                .nationality("US")
                .build();

        Client domain = mapper.toDomainModel(entity);

        assertEquals(id.toString(), domain.getId());
        assertEquals("Alice", domain.getFirstName());
        assertEquals("Johnson", domain.getLastName());
        assertEquals(LocalDate.of(1985, 5, 20), domain.getBirthDate());
        assertEquals("F", domain.getGender());
        assertEquals("US", domain.getNationality());
    }

    @Test
    void toDomainModel_shouldHandleNullId() {

        ClientEntity entity = ClientEntity.builder()
                .id(null)
                .firstName("Bob")
                .lastName("Marley")
                .build();

        Client domain = mapper.toDomainModel(entity);

        assertNull(domain.getId());
        assertEquals("Bob", domain.getFirstName());
        assertEquals("Marley", domain.getLastName());
    }
}
