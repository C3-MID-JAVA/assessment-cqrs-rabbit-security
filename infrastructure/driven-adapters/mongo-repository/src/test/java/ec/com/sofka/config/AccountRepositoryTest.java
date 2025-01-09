package ec.com.sofka.config;
import ec.com.sofka.account.Account;
import ec.com.sofka.data.AccountEntity;
import ec.com.sofka.database.account.IMongoAccountRepository;
import ec.com.sofka.mapper.AccountMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

@ActiveProfiles("test")
@DataMongoTest
@AutoConfigureDataMongo
@ContextConfiguration(classes = TestMongoConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountRepositoryTest {

    @Autowired
    private IMongoAccountRepository repository;

    @Test
    void testCrudOperations() {
        Account account = new Account("675dbabe03edcf54111957fe", BigDecimal.valueOf(500), "1234567890", "Anderson Zambrano");


        AccountEntity accountEntity = AccountMapper.accountToAccountEntity(account);
        Mono<AccountEntity> saveMono = repository.save(accountEntity);

        Mono<AccountEntity> findMono = saveMono
                .then(repository.findByAccountNumber("1234567890"));

        Mono<AccountEntity> updateMono = findMono.flatMap(foundAccount -> {
            foundAccount.setBalance(BigDecimal.valueOf(1000));
            return repository.save(foundAccount);
        });

        Mono<Void> deleteMono = updateMono.flatMap(updatedAccount ->
                repository.deleteById(updatedAccount.getId())
        );

        Flux<AccountEntity> verifyDeletion = deleteMono
                .thenMany(repository.findAll());

        StepVerifier.create(saveMono)
                .expectNextMatches(savedAccount -> savedAccount.getId() != null)
                .verifyComplete();

        StepVerifier.create(findMono)
                .expectNextMatches(foundAccount -> foundAccount.getBalance().compareTo(BigDecimal.valueOf(500)) ==0)
                .verifyComplete();

        StepVerifier.create(updateMono)
                .expectNextMatches(updatedAccount -> updatedAccount.getBalance().compareTo(BigDecimal.valueOf(1000)) ==0 )
                .verifyComplete();

        StepVerifier.create(verifyDeletion)
                .expectNextCount(0)
                .verifyComplete();
    }
}
