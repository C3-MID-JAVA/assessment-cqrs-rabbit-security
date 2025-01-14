package ec.com.sofka.config;

import ec.com.sofka.TestMongoConfig;
import ec.com.sofka.data.EventEntity;
import ec.com.sofka.database.events.IEventMongoRepository;
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

@ActiveProfiles("test")
@DataMongoTest
@AutoConfigureDataMongo
@ContextConfiguration(classes = TestMongoConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EventMongoRepositoryTest {

    @Autowired
    private IEventMongoRepository eventRepository;

    private EventEntity event1;
    private EventEntity event2;
    private EventEntity event3;

    @BeforeAll
    void setup() {
        event1 = new EventEntity(
                "1",
                "customer-123",
                "AggregateCreated",
                "{\"name\":\"John Doe\"}",
                "2025-01-01T12:00:00Z",
                1L,
                "CustomerAggregate"
        );

        event2 = new EventEntity(
                "2",
                "customer-123",
                "AccountUpdated",
                "{\"balance\":1000}",
                "2025-01-02T12:00:00Z",
                2L,
                "CustomerAggregate"
        );

        event3 = new EventEntity(
                "3",
                "order-456",
                "OrderPlaced",
                "{\"orderId\":\"ORD123\"}",
                "2025-01-03T12:00:00Z",
                1L,
                "OrderAggregate"
        );
    }

    @BeforeEach
    void init() {
        eventRepository.deleteAll().block();
        eventRepository.saveAll(Flux.just(event1, event2, event3)).blockLast();
    }

    @Test
    void findAllByAggregateId_shouldReturnEventsForSpecificAggregateId() {
        StepVerifier.create(eventRepository.findAllByAggregateId("customer-123"))
                .expectNextMatches(event -> event.getEventType().equals("AggregateCreated") && event.getVersion() == 1L)
                .expectNextMatches(event -> event.getEventType().equals("AccountUpdated") && event.getVersion() == 2L)
                .verifyComplete();
    }

    @Test
    void findAllByAggregateId_shouldReturnEmpty_whenAggregateIdDoesNotExist() {
        StepVerifier.create(eventRepository.findAllByAggregateId("non-existent-id"))
                .verifyComplete();
    }

    @Test
    void findAllByAggregateRootName_shouldReturnEventsForSpecificAggregateRootName() {
        StepVerifier.create(eventRepository.findAllByAggregateRootName("CustomerAggregate"))
                .expectNextMatches(event -> event.getAggregateId().equals("customer-123"))
                .expectNextMatches(event -> event.getAggregateId().equals("customer-123"))
                .verifyComplete();
    }

    @Test
    void findAllByAggregateRootName_shouldReturnEmpty_whenAggregateRootNameDoesNotExist() {
        StepVerifier.create(eventRepository.findAllByAggregateRootName("NonExistentAggregate"))
                .verifyComplete();
    }

    @Test
    void save_shouldPersistEvent() {
        EventEntity newEvent = new EventEntity(
                "4",
                "order-789",
                "OrderCancelled",
                "{\"reason\":\"Customer Request\"}",
                "2025-01-04T12:00:00Z",
                1L,
                "OrderAggregate"
        );

        StepVerifier.create(eventRepository.save(newEvent))
                .expectNextMatches(event -> event.getId().equals("4") && event.getAggregateId().equals("order-789"))
                .verifyComplete();

        StepVerifier.create(eventRepository.findById("4"))
                .expectNextMatches(event -> event.getEventType().equals("OrderCancelled") && event.getVersion() == 1L)
                .verifyComplete();
    }

    @Test
    void delete_shouldRemoveEvent() {
        StepVerifier.create(eventRepository.deleteById("1"))
                .verifyComplete();

        StepVerifier.create(eventRepository.findById("1"))
                .verifyComplete();
    }
}
