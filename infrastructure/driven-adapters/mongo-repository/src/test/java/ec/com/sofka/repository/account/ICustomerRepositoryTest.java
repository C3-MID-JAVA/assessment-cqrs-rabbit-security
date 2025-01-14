package ec.com.sofka.repository.account;

import ec.com.sofka.TestMongoConfig;
import ec.com.sofka.data.CustomerEntity;
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

@ActiveProfiles("test")
@ContextConfiguration(classes = TestMongoConfig.class)
@AutoConfigureDataMongo
@DataMongoTest
public class ICustomerRepositoryTest {
    private final ICustomerRepository repository;

    @Autowired
    public ICustomerRepositoryTest(ICustomerRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    public void setUp() {
        repository.deleteAll().subscribe();
    }

    @Test
    public void TestSaveAndFindConsumer() {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setIdentityCard("1310000000");
        customerEntity.setFirstName("Diego");
        customerEntity.setLastName("Loor");
        customerEntity.setStatus(StatusEnum.ACTIVE);

        repository.save(customerEntity).subscribe();

        StepVerifier.create(repository.findAll())
                .expectNextMatches(item -> item.getIdentityCard().equals(customerEntity.getIdentityCard()))
                .expectComplete()
                .verify();
    }

    @Test
    public void TestDeleteConsumer() {
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