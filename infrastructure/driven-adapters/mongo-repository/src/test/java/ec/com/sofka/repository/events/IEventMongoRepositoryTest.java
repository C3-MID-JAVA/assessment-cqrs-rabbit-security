package ec.com.sofka.repository.events;

import ec.com.sofka.TestMongoConfig;
import ec.com.sofka.data.EventEntity;
import ec.com.sofka.utils.enums.EventsDetailsEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Date;

@ActiveProfiles("test")
@ContextConfiguration(classes = TestMongoConfig.class)
@AutoConfigureDataMongo
@DataMongoTest
public class IEventMongoRepositoryTest
{
    private final IEventMongoRepository repository;

    @Autowired
    public IEventMongoRepositoryTest(IEventMongoRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    public void setUp() {
        repository.deleteAll().subscribe();
    }

    @Test
    public void TestSaveAndFindEvent() {
        EventEntity eventEntity = new EventEntity();
        eventEntity.setEventData("TEST");
        eventEntity.setEventType(EventsDetailsEnum.USER_UPDATED.getEventType());
        eventEntity.setTimestamp(new Date().toString());
        eventEntity.setAggregateId("1");

        repository.save(eventEntity).subscribe();

        StepVerifier.create(repository.findAll())
                .expectNextMatches(item -> item.getEventType().equals(eventEntity.getEventType()))
                .expectComplete()
                .verify();
    }

    @Test
    public void TestDeleteEvent() {
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