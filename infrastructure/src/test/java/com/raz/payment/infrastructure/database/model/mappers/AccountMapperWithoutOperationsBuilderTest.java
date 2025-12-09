package com.raz.payment.infrastructure.database.model.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.raz.payment.domain.model.account.Account;
import com.raz.payment.domain.model.client.Client;
import com.raz.payment.infrastructure.database.model.entity.AccountEntity;
import com.raz.payment.infrastructure.database.model.entity.ClientEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountMapperWithoutOperationsBuilderTest {

    private ClientMapper clientMapper;
    private AccountMapperWithoutOperationsBuilder mapper;

    @BeforeEach
    void setUp() {
        clientMapper = new ClientMapper(); 
        mapper = new AccountMapperWithoutOperationsBuilder(clientMapper);
    }

    // -------------------------------------------------------
    // DOMAIN → ENTITY
    // -------------------------------------------------------
    @Test
    void toAccountEntityBuilder_shouldMapAllFieldsCorrectly() {

        String uuidStr = UUID.randomUUID().toString();

        Client client1 = Client.builder()
                .id(UUID.randomUUID().toString())
                .firstName("John")
                .lastName("Doe")
                .build();

        Client client2 = Client.builder()
                .id(UUID.randomUUID().toString())
                .firstName("Emma")
                .lastName("Stone")
                .build();

        Account account = Account.builder()
                .id(uuidStr)
                .accountNumber("ACC999")
                .currentBalance(new BigDecimal(1500.00))
                .owners(List.of(client1, client2))
                .build();

        AccountEntity entity = mapper.toAccountEntityBuilder(account).build();

        assertEquals(UUID.fromString(uuidStr), entity.getId());
        assertEquals("ACC999", entity.getAccountNumber());
        assertEquals(new BigDecimal(1500.00), entity.getCurrentBalance());
        assertEquals(2, entity.getOwners().size());

        ClientEntity e1 = entity.getOwners().get(0);
        ClientEntity e2 = entity.getOwners().get(1);

        assertEquals(client1.getFirstName(), e1.getFirstName());
        assertEquals(client2.getFirstName(), e2.getFirstName());
    }

    @Test
    void toAccountEntityBuilder_shouldHandleNullId() {
        Account account = Account.builder()
                .id(null)
                .accountNumber("ACC123")
                .currentBalance(new BigDecimal(500.00))
                .owners(List.of())
                .build();

        AccountEntity entity = mapper.toAccountEntityBuilder(account).build();

        assertNull(entity.getId());
        assertEquals("ACC123", entity.getAccountNumber());
        assertEquals(new BigDecimal(500.00), entity.getCurrentBalance());
        assertTrue(entity.getOwners().isEmpty());
    }

    // -------------------------------------------------------
    // ENTITY → DOMAIN
    // -------------------------------------------------------
    @Test
    void toAccountBuilder_shouldMapAllFieldsCorrectly() {
        UUID uuid = UUID.randomUUID();

        ClientEntity ce1 = ClientEntity.builder()
                .id(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .build();

        ClientEntity ce2 = ClientEntity.builder()
                .id(UUID.randomUUID())
                .firstName("Emma")
                .lastName("Stone")
                .build();

        AccountEntity entity = AccountEntity.builder()
                .id(uuid)
                .accountNumber("ACC777")
                .currentBalance(new BigDecimal(800.00))
                .owners(List.of(ce1, ce2))
                .build();

        Account account = mapper.toAccountBuilder(entity).build();

        assertEquals(uuid.toString(), account.getId());
        assertEquals("ACC777", account.getAccountNumber());
        assertEquals(new BigDecimal(800.00), account.getCurrentBalance());
        assertEquals(2, account.getOwners().size());

        Client d1 = account.getOwners().get(0);
        Client d2 = account.getOwners().get(1);

        assertEquals("John", d1.getFirstName());
        assertEquals("Emma", d2.getFirstName());
    }

    @Test
    void toAccountBuilder_shouldHandleNullId() {
        AccountEntity entity = AccountEntity.builder()
                .id(null)
                .accountNumber("ACCNOID")
                .currentBalance(new BigDecimal(900.00))
                .owners(List.of())
                .build();

        Account account = mapper.toAccountBuilder(entity).build();

        assertNull(account.getId());
        assertEquals("ACCNOID", account.getAccountNumber());
        assertEquals(new BigDecimal(900.00), account.getCurrentBalance());
        assertTrue(account.getOwners().isEmpty());
    }
}
