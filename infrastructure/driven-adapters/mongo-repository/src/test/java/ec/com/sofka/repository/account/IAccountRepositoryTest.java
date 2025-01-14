package ec.com.sofka.repository.account;

import ec.com.sofka.TestMongoConfig;
import ec.com.sofka.data.AccountEntity;
import ec.com.sofka.utils.enums.StatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

@ActiveProfiles("test")
@ContextConfiguration(classes = TestMongoConfig.class)
@AutoConfigureDataMongo
@DataMongoTest
public class IAccountRepositoryTest {
    private final IAccountRepository repository;

    @Autowired
    public IAccountRepositoryTest(IAccountRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    public void setUp() {
        repository.deleteAll().subscribe();
    }

    @Test
    public void TestSaveAndFindAccount() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountNumber("2200000000");
        accountEntity.setBalance(new BigDecimal(100));
        accountEntity.setStatus(StatusEnum.ACTIVE);

        repository.save(accountEntity).subscribe();

        StepVerifier.create(repository.findAll())
                .expectNextMatches(item -> item.getAccountNumber().equals(accountEntity.getAccountNumber()))
                .expectComplete()
                .verify();
    }

    @Test
    public void TestDeleteAccount() {
        Mono<Void> deleteResult = repository.deleteAll();

        StepVerifier.create(deleteResult)
                .expectComplete()
                .verify();

        StepVerifier.create(repository.findAll())
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }

}