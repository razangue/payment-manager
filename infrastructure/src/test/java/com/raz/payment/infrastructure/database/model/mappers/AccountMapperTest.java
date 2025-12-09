package com.raz.payment.infrastructure.database.model.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.raz.payment.domain.model.account.Account;
import com.raz.payment.domain.model.client.Client;
import com.raz.payment.domain.model.operation.*;
import com.raz.payment.infrastructure.database.model.entity.AccountEntity;
import com.raz.payment.infrastructure.database.model.entity.AccountOperationEntity;

class AccountMapperTest {

    private ClientMapper clientMapper;
    private AccountMapperWithoutOperationsBuilder withoutOpMapper;
    private AccountOperationMapper opMapper;
    private AccountMapper accountMapper;

    @BeforeEach
    void setUp() {
        clientMapper = new ClientMapper();
        withoutOpMapper = new AccountMapperWithoutOperationsBuilder(clientMapper);
        opMapper = new AccountOperationMapper(withoutOpMapper);
        accountMapper = new AccountMapper(withoutOpMapper, opMapper);
    }

    private Account.AccountBuilder createAccountBuilder(String number) {
        Client owner = Client.builder()
                .id(UUID.randomUUID().toString())
                .firstName("John")
                .lastName("Doe")
                .build();

        return Account.builder()
                .id(UUID.randomUUID().toString())
                .accountNumber(number)
                .currentBalance(new BigDecimal(500))
                .owners(List.of(owner))
                .operations(List.of());
    }

    private AccountOperation createDeposit(Account account) {
        return DepositOperation.builder()
                .id(1L)
                .sourceAccount(account)
                .accountOperationType(AccountOperationType.DEPOSIT)
                .amount(new BigDecimal(100))
                .date(LocalDateTime.now())
                .build();
    }

    private AccountOperation createPayment(Account source, Account receiver) {
        return PaymentOperation.builder()
                .id(2L)
                .sourceAccount(source)
                .receivingAccount(receiver)
                .amount(new BigDecimal(200))
                .accountOperationType(AccountOperationType.PAYMENT)
                .date(LocalDateTime.now())
                .build();
    }

    // ---------------------------------------------------------
    // DOMAIN → ENTITY
    // ---------------------------------------------------------

    @Test
    void toEntity_shouldMapAccountWithOperations() {
        Account.AccountBuilder acc1 = createAccountBuilder("ACC100");
         Account.AccountBuilder acc2 = createAccountBuilder("ACC200");

        AccountOperation op1 = createDeposit(acc1.build());
        AccountOperation op2 = createPayment(acc1.build(), acc2.build());

        Account account = acc1.operations(List.of(op1, op2))
                .build();

        AccountEntity entity = accountMapper.toEntity(account);

        assertNotNull(entity);
        assertEquals("ACC100", entity.getAccountNumber());
        assertEquals(2, entity.getOperations().size());

        AccountOperationEntity e1 = entity.getOperations().get(0);
        AccountOperationEntity e2 = entity.getOperations().get(1);

        assertEquals(AccountOperationType.DEPOSIT, e1.getOperationType());
        assertEquals(AccountOperationType.PAYMENT, e2.getOperationType());

        // receivingAccount must be mapped for PAYMENT
        assertNotNull(e2.getReceivingAccount());
    }

    // ---------------------------------------------------------
    // ENTITY → DOMAIN
    // ---------------------------------------------------------

    @Test
    void toDomainModel_shouldMapEntityWithOperations() {
        Account acc1 = createAccountBuilder("ACC300").build();
        Account acc2 = createAccountBuilder("ACC400").build();

        AccountOperationEntity op1 = opMapper.toEntity(createDeposit(acc1));
        AccountOperationEntity op2 = opMapper.toEntity(createPayment(acc1, acc2));

        AccountEntity entity = withoutOpMapper
                .toAccountEntityBuilder(acc1)
                .operations(List.of(op1, op2))
                .build();

        Account domain = accountMapper.toDomainModel(entity);

        assertNotNull(domain);
        assertEquals("ACC300", domain.getAccountNumber());
        assertEquals(2, domain.getOperations().size());

        AccountOperation d1 = domain.getOperations().get(0);
        AccountOperation d2 = domain.getOperations().get(1);

        assertInstanceOf(DepositOperation.class, d1);
        assertInstanceOf(PaymentOperation.class, d2);

        // receivingAccount must be mapped
        PaymentOperation payment = (PaymentOperation) d2;
        assertNotNull(payment.getReceivingAccount());
    }

    // ---------------------------------------------------------
    // EMPTY LISTS
    // ---------------------------------------------------------

    @Test
    void toEntity_shouldHandleNoOperations() {
        Account account = createAccountBuilder("ACCEMPTY").build();

        AccountEntity entity = accountMapper.toEntity(account);

        assertNotNull(entity.getOperations());
        assertTrue(entity.getOperations().isEmpty());
    }

    @Test
    void toDomainModel_shouldHandleNoOperations() {
        Account account = createAccountBuilder("ACCEMPTY").build();
        AccountEntity entity = withoutOpMapper.toAccountEntityBuilder(account)
                .operations(List.of())
                .build();

        Account domain = accountMapper.toDomainModel(entity);

        assertNotNull(domain.getOperations());
        assertTrue(domain.getOperations().isEmpty());
    }
}
