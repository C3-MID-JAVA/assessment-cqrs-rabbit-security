package ec.com.sofka.database.account;


import ec.com.sofka.data.AccountEntity;
import ec.com.sofka.database.TestMongoConfig;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ActiveProfiles("test")
@DataMongoTest
@AutoConfigureDataMongo
@ContextConfiguration(classes = TestMongoConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IAccountMongoRepositoryTest {

    @Autowired
    private IAccountMongoRepository accountMongoRepository;

    @BeforeEach
    void setUp() {
        accountMongoRepository.deleteAll().block();
    }

    @AfterEach
    void tearDown() {
        accountMongoRepository.deleteAll().block();
    }

    @Test
    @DisplayName("Should found an account on the database with the same account number")
    void findByAccountNumber() {
        AccountEntity accountModel = new AccountEntity();
        accountModel.setAccountNumber("6363636363");
        Mono<AccountEntity> accountModelSaved = accountMongoRepository.save(accountModel);
        StepVerifier.create(accountModelSaved)
                .expectNext(accountModel)
                .verifyComplete();

        Mono<AccountEntity> accountModelFound = accountMongoRepository.findByAccountNumber(accountModel.getAccountNumber());
        StepVerifier.create(accountModelFound)
                .expectNextMatches(accM -> accM.getAccountNumber().equals("6363636363"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should NOT found an account the database with the same account number WHEN account not exist")
    void findByAccountNumber_Notfound() {
        Mono<AccountEntity> accountModelResponse = accountMongoRepository.findByAccountNumber("9999999999");

        StepVerifier.create(accountModelResponse)
                .expectNextCount(0)
                .verifyComplete();
    }
}