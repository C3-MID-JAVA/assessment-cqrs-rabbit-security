package ec.com.sofka.config;

import ec.com.sofka.TestMongoConfig;

import ec.com.sofka.data.TransactionEntity;
import ec.com.sofka.database.bank.TransactionMongoRepository;
import ec.com.sofka.transaction.TransactionType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ActiveProfiles("test")
@DataMongoTest
@AutoConfigureDataMongo
@ContextConfiguration(classes = TestMongoConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionMongoRepositoryTest {

    @Autowired
    private TransactionMongoRepository transactionRepository;

    private TransactionEntity transaction1;
    private TransactionEntity transaction2;

    @BeforeAll
    void setup() {
        transaction1 = new TransactionEntity(
                "1",
                BigDecimal.valueOf(100.0),
                BigDecimal.valueOf(2.0),
                BigDecimal.valueOf(98.0),
                TransactionType.ATM_DEPOSIT,
                LocalDateTime.now(),
                "ACC123"
        );
        transaction2 = new TransactionEntity(
                "2",
                BigDecimal.valueOf(50.0),
                BigDecimal.valueOf(1.0),
                BigDecimal.valueOf(49.0),
                TransactionType.ATM_WITHDRAWAL,
                LocalDateTime.now(),
                "ACC123"
        );
    }

    @BeforeEach
    void init() {
        transactionRepository.deleteAll().block();
        transactionRepository.saveAll(Flux.just(transaction1, transaction2)).blockLast();
    }

    @Test
    void findById_shouldReturnTransaction_whenTransactionExists() {
        StepVerifier.create(transactionRepository.findById("1"))
                .expectNextMatches(transaction -> transaction.getAmount().equals(BigDecimal.valueOf(100.0))
                        && transaction.getFee().equals(BigDecimal.valueOf(2.0))
                        && transaction.getType() == TransactionType.ATM_DEPOSIT
                        && transaction.getAccountId().equals("ACC123"))
                .verifyComplete();
    }

    @Test
    void findById_shouldReturnEmpty_whenTransactionDoesNotExist() {
        StepVerifier.create(transactionRepository.findById("99"))
                .verifyComplete();
    }

    @Test
    void findAllByAccountId_shouldReturnTransactions_whenAccountHasTransactions() {
        StepVerifier.create(transactionRepository.findAllByAccountId("ACC123"))
                .expectNextMatches(transaction -> transaction.getId().equals("1"))
                .expectNextMatches(transaction -> transaction.getId().equals("2"))
                .verifyComplete();
    }

    @Test
    void findAllByAccountId_shouldReturnEmpty_whenAccountHasNoTransactions() {
        StepVerifier.create(transactionRepository.findAllByAccountId("NON_EXISTENT_ACC"))
                .verifyComplete();
    }

    @Test
    void save_shouldPersistTransaction() {
        TransactionEntity newTransaction = new TransactionEntity(
                "3",
                BigDecimal.valueOf(200.0),
                BigDecimal.valueOf(5.0),
                BigDecimal.valueOf(195.0),
                TransactionType.ATM_DEPOSIT,
                LocalDateTime.now(),
                "ACC789"
        );

        StepVerifier.create(transactionRepository.save(newTransaction))
                .expectNextMatches(transaction -> transaction.getId().equals("3")
                        && transaction.getAmount().equals(BigDecimal.valueOf(200.0))
                        && transaction.getFinalAmount().equals(BigDecimal.valueOf(195.0)))
                .verifyComplete();

        StepVerifier.create(transactionRepository.findById("3"))
                .expectNextMatches(transaction -> transaction.getType() == TransactionType.ATM_DEPOSIT
                        && transaction.getAccountId().equals("ACC789"))
                .verifyComplete();
    }

    @Test
    void delete_shouldRemoveTransaction() {
        StepVerifier.create(transactionRepository.deleteById("1"))
                .verifyComplete();

        StepVerifier.create(transactionRepository.findById("1"))
                .verifyComplete();
    }
}