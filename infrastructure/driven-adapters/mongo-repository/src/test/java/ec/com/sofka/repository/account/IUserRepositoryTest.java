package ec.com.sofka.repository.account;

import ec.com.sofka.TestMongoConfig;
import ec.com.sofka.data.UserEntity;
import ec.com.sofka.utils.enums.RoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ActiveProfiles("test")
@ContextConfiguration(classes = TestMongoConfig.class)
@AutoConfigureDataMongo
@DataMongoTest
public class IUserRepositoryTest {

    private final IUserRepository repository;

    @Autowired
    public IUserRepositoryTest(IUserRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    public void setUp() {
        repository.deleteAll().subscribe();
    }

    @Test
    public void TestSaveAndFindUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("diego.loor@sofka.com.co");
        userEntity.setFirstName("Diego");
        userEntity.setLastName("Loor");
        userEntity.setPassword("Test123.");
        userEntity.setRole(RoleEnum.ROLE_BANK_TELLER);

        repository.save(userEntity).subscribe();

        StepVerifier.create(repository.findAll())
                .expectNextMatches(item -> item.getEmail().equals(userEntity.getEmail()))
                .expectComplete()
                .verify();
    }

    @Test
    public void TestDeleteUser() {
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