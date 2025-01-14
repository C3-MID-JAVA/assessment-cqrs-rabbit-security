package ec.com.sofka.database.events;

import ec.com.sofka.data.AccountEntity;
import ec.com.sofka.data.EventEntity;
import ec.com.sofka.database.TestMongoConfig;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataMongoTest
@AutoConfigureDataMongo
@ContextConfiguration(classes = TestMongoConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IEventMongoRepositoryTest {

    @Autowired
    private IEventMongoRepository eventRepository;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll().subscribe();
    }

    @AfterEach
    void tearDown() {
        eventRepository.deleteAll().subscribe();
    }

    @Test
    @DisplayName("Should found aggregates on the database")
    void findAllByAggregateId() {
        EventEntity eventModel = new EventEntity();
        eventModel.setAggregateId("6363636363");
        Mono<EventEntity> eventModelSaved = eventRepository.save(eventModel);
        StepVerifier.create(eventModelSaved)
                .expectNext(eventModel)
                .verifyComplete();

        Flux<EventEntity> eventModelFound = eventRepository.findAllByAggregateId(eventModel.getAggregateId());
        StepVerifier.create(eventModelFound)
                .expectNextMatches(accM -> accM.getAggregateId().equals("6363636363"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should found aggregates on the database by AggregateRootName")
    void findAllByAggregateRootName() {
        EventEntity eventModel = new EventEntity();
        eventModel.setAggregateRootName("agregateName");
        Mono<EventEntity> eventModelSaved = eventRepository.save(eventModel);
        StepVerifier.create(eventModelSaved)
                .expectNext(eventModel)
                .verifyComplete();

        Flux<EventEntity> eventModelFound = eventRepository.findAllByAggregateRootName(eventModel.getAggregateRootName());
        StepVerifier.create(eventModelFound)
                .expectNextMatches(accM -> accM.getAggregateRootName().equals("agregateName"))
                .verifyComplete();
    }
}