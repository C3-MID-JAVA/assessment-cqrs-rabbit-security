package ec.com.sofka.config;

import ec.com.sofka.TestMongoConfig;
import ec.com.sofka.data.AccountEntity;
import ec.com.sofka.database.bank.AccountMongoRepository;
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

@ActiveProfiles("test")
@DataMongoTest
@AutoConfigureDataMongo
@ContextConfiguration(classes = TestMongoConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountMongoRepositoryTest {

    @Autowired
    AccountMongoRepository accountRepository;

    private AccountEntity account1;
    private AccountEntity account2;

    @BeforeAll
    void setup(){
        account1 = new AccountEntity("1", "ACC123", BigDecimal.valueOf(500.0), "USER1");
        account2 = new AccountEntity("2", "ACC456", BigDecimal.valueOf(500.0), "USER2");
    }

    @BeforeEach
    void init() {
        accountRepository.deleteAll().block();
        accountRepository.saveAll(Flux.just(account1, account2)).blockLast();
    }

    @Test
    void findById_shouldReturnAccount_whenAccountExists() {
        StepVerifier.create(accountRepository.findById("1"))
                .expectNextMatches(account -> account.getAccountNumber().equals("ACC123")
                        && account.getBalance().equals(BigDecimal.valueOf(500.0))
                        && account.getUserId().equals("USER1"))
                .verifyComplete();
    }

    @Test
    void findById_shouldReturnEmpty_whenAccountDoesNotExist() {
        StepVerifier.create(accountRepository.findById("99"))
                .verifyComplete();
    }

    @Test
    void findByUserId_shouldReturnAccounts_whenUserHasAccounts() {
        StepVerifier.create(accountRepository.findByUserId("USER1"))
                .expectNextMatches(account -> account.getAccountNumber().equals("ACC123"))
                .verifyComplete();
    }

    @Test
    void findByUserId_shouldReturnEmpty_whenUserHasNoAccounts() {
        StepVerifier.create(accountRepository.findByUserId("NON_EXISTENT_USER"))
                .verifyComplete();
    }

    @Test
    void findByAccountNumber_shouldReturnAccount_whenAccountNumberExists() {
        StepVerifier.create(accountRepository.findByAccountNumber("ACC456"))
                .expectNextMatches(account -> account.getUserId().equals("USER2"))
                .verifyComplete();
    }

    @Test
    void findByAccountNumber_shouldReturnEmpty_whenAccountNumberDoesNotExist() {
        StepVerifier.create(accountRepository.findByAccountNumber("INVALID_ACC"))
                .verifyComplete();
    }

    @Test
    void save_shouldPersistAccount() {
        AccountEntity newAccount = new AccountEntity(
                "3",
                "ACC789",
                BigDecimal.valueOf(300.0),
                "USER3");

        StepVerifier.create(accountRepository.save(newAccount))
                .expectNextMatches(account -> account.getId().equals("3")
                        && account.getAccountNumber().equals("ACC789"))
                .verifyComplete();

        StepVerifier.create(accountRepository.findById("3"))
                .expectNextMatches(account ->
                        account.getBalance().equals(BigDecimal.valueOf(300.0)) && account.getUserId().equals("USER3"))
                .verifyComplete();
    }

    @Test
    void delete_shouldRemoveAccount() {
        StepVerifier.create(accountRepository.deleteById("1"))
                .verifyComplete();

        StepVerifier.create(accountRepository.findById("1"))
                .verifyComplete();
    }
}