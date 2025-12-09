package com.raz.payment.infrastructure.database.model.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.raz.payment.domain.model.account.Account;
import com.raz.payment.domain.model.client.Client;
import com.raz.payment.domain.model.operation.AccountOperation;
import com.raz.payment.domain.model.operation.AccountOperationType;
import com.raz.payment.domain.model.operation.DepositOperation;
import com.raz.payment.domain.model.operation.PaymentOperation;
import com.raz.payment.domain.model.operation.WithdrawalOperation;
import com.raz.payment.infrastructure.database.model.entity.AccountEntity;
import com.raz.payment.infrastructure.database.model.entity.AccountOperationEntity;

class AccountOperationMapperTest {

        private ClientMapper clientMapper;
        private AccountMapperWithoutOperationsBuilder accountMapper;
        private AccountOperationMapper mapper;

        @BeforeEach
        void setUp() {
                clientMapper = new ClientMapper();
                accountMapper = new AccountMapperWithoutOperationsBuilder(clientMapper);
                mapper = new AccountOperationMapper(accountMapper);
        }

        private Account createAccount(String id, String number) {
                Client owner = Client.builder()
                                .id(UUID.randomUUID().toString())
                                .firstName("John")
                                .lastName("Doe")
                                .build();

                return Account.builder()
                                .id(id)
                                .accountNumber(number)
                                .currentBalance(new BigDecimal(1000))
                                .owners(List.of(owner))
                                .build();
        }

        // ---------------------------------------------------------
        // DOMAIN → ENTITY
        // ---------------------------------------------------------

        @Test
        void toEntity_shouldMapDepositOperation() {
                Account source = createAccount(UUID.randomUUID().toString(), "ACC001");

                DepositOperation op = DepositOperation.builder()
                                .id(10L)
                                .sourceAccount(source)
                                .amount(new BigDecimal(200))
                                .date(LocalDateTime.now())
                                .accountOperationType(AccountOperationType.DEPOSIT)
                                .build();

                AccountOperationEntity entity = mapper.toEntity(op);

                assertEquals(10L, entity.getId());
                assertEquals(AccountOperationType.DEPOSIT, entity.getOperationType());
                assertEquals(new BigDecimal(200), entity.getAmount());
                assertNotNull(entity.getSourceAccount());
                assertNull(entity.getReceivingAccount());
        }

        @Test
        void toEntity_shouldMapWithdrawalOperation() {
                Account source = createAccount(UUID.randomUUID().toString(), "ACC002");

                WithdrawalOperation op = WithdrawalOperation.builder()
                                .id(20L)
                                .sourceAccount(source)
                                .amount(new BigDecimal(300))
                                .date(LocalDateTime.now())
                                .accountOperationType(AccountOperationType.WITHDRAWAL)
                                .build();

                AccountOperationEntity entity = mapper.toEntity(op);

                assertEquals(20L, entity.getId());
                assertEquals(AccountOperationType.WITHDRAWAL, entity.getOperationType());
                assertEquals(new BigDecimal(300), entity.getAmount());
                assertNotNull(entity.getSourceAccount());
                assertNull(entity.getReceivingAccount());
        }

        @Test
        void toEntity_shouldMapPaymentOperation() {
                Account source = createAccount(UUID.randomUUID().toString(), "ACC003");
                Account receiver = createAccount(UUID.randomUUID().toString(), "ACC999");

                PaymentOperation op = PaymentOperation.builder()
                                .id(30L)
                                .sourceAccount(source)
                                .receivingAccount(receiver)
                                .amount(new BigDecimal(500))
                                .date(LocalDateTime.now())
                                .accountOperationType(AccountOperationType.PAYMENT)
                                .build();

                AccountOperationEntity entity = mapper.toEntity(op);

                assertEquals(30L, entity.getId());
                assertEquals(AccountOperationType.PAYMENT, entity.getOperationType());
                assertNotNull(entity.getSourceAccount());
                assertNotNull(entity.getReceivingAccount());
        }

        // ---------------------------------------------------------
        // ENTITY → DOMAIN
        // ---------------------------------------------------------

        @Test
        void toDomainModel_shouldMapDepositEntity() {
                AccountEntity source = accountMapper
                                .toAccountEntityBuilder(createAccount(UUID.randomUUID().toString(), "ACC050"))
                                .build();

                AccountOperationEntity entity = AccountOperationEntity.builder()
                                .id(100L)
                                .sourceAccount(source)
                                .operationType(AccountOperationType.DEPOSIT)
                                .amount(new BigDecimal(999))
                                .date(LocalDateTime.now())
                                .build();

                AccountOperation op = mapper.toDomainModel(entity);

                assertInstanceOf(DepositOperation.class, op);
                assertEquals(100L, op.getId());
                assertEquals(new BigDecimal(999), op.getAmount());
        }

        @Test
        void toDomainModel_shouldMapWithdrawalEntity() {
                AccountEntity source = accountMapper
                                .toAccountEntityBuilder(createAccount(UUID.randomUUID().toString(), "ACC051"))
                                .build();

                AccountOperationEntity entity = AccountOperationEntity.builder()
                                .id(200L)
                                .sourceAccount(source)
                                .operationType(AccountOperationType.WITHDRAWAL)
                                .amount(new BigDecimal(50))
                                .date(LocalDateTime.now())
                                .build();

                AccountOperation op = mapper.toDomainModel(entity);

                assertInstanceOf(WithdrawalOperation.class, op);
                assertEquals(new BigDecimal(50), op.getAmount());
        }

        @Test
        void toDomainModel_shouldMapPaymentEntity() {
                AccountEntity source = accountMapper
                                .toAccountEntityBuilder(createAccount(UUID.randomUUID().toString(), "SRC"))
                                .build();
                AccountEntity receiver = accountMapper
                                .toAccountEntityBuilder(createAccount(UUID.randomUUID().toString(), "RCV"))
                                .build();

                AccountOperationEntity entity = AccountOperationEntity.builder()
                                .id(300L)
                                .sourceAccount(source)
                                .receivingAccount(receiver)
                                .operationType(AccountOperationType.PAYMENT)
                                .amount(new BigDecimal(123))
                                .date(LocalDateTime.now())
                                .build();

                AccountOperation op = mapper.toDomainModel(entity);

                assertInstanceOf(PaymentOperation.class, op);
                assertNotNull(((PaymentOperation) op).getReceivingAccount());
                assertEquals(new BigDecimal(123), op.getAmount());
        }
}
