package ec.com.sofka.repository.account;

import ec.com.sofka.TestMongoConfig;
import ec.com.sofka.data.ErrorLogEntity;
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
public class IErrorLogRepositoryTest {
    private final IErrorLogRepository repository;

    @Autowired
    public IErrorLogRepositoryTest(IErrorLogRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    public void setUp() {
        repository.deleteAll().subscribe();
    }

    @Test
    public void TestSaveAndFindUser() {
        ErrorLogEntity errorLogEntity = new ErrorLogEntity();
        errorLogEntity.setError("User not found");
        errorLogEntity.setProccess("Find User");
        errorLogEntity.setTimestamp("13-01-2025 12:00:00");

        repository.save(errorLogEntity).subscribe();

        StepVerifier.create(repository.findAll())
                .expectNextMatches(item -> item.getError().equals(errorLogEntity.getError()))
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