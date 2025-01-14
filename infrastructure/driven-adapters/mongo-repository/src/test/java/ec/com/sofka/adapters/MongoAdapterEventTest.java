package ec.com.sofka.adapters;

import ec.com.sofka.EventMongoAdapter;
import ec.com.sofka.data.EventEntity;
import ec.com.sofka.database.events.IEventMongoRepository;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.JSONMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MongoAdapterEventTest {

    @Mock
    private IEventMongoRepository repository;

    @Mock
    private JSONMap mapper;

    @InjectMocks
    private EventMongoAdapter eventMongoAdapter;

    private DomainEvent domainEvent;
    private EventEntity eventEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        domainEvent = new DomainEvent("TestEvent") {
            @Override
            public String getAggregateRootId() {
                return "aggregateId";
            }
        };
        domainEvent.setAggregateRootId("aggregateId");
        domainEvent.setVersion(1L);
        eventEntity = new EventEntity(
                domainEvent.getEventId(),
                domainEvent.getAggregateRootId(),
                domainEvent.getEventType(),
                "{}",
                Instant.now().toString(),
                domainEvent.getVersion()
        );
    }

    @Test
    void testSaveEventSuccess() {
        when(repository.save(any(EventEntity.class))).thenReturn(Mono.just(eventEntity));

        Mono<DomainEvent> result = eventMongoAdapter.save(domainEvent);

        StepVerifier.create(result)
                .expectNextMatches(savedEvent -> savedEvent.getEventId().equals(domainEvent.getEventId()))
                .verifyComplete();
    }

    @Test
    void testSaveEventFailure() {
        when(repository.save(any(EventEntity.class))).thenReturn(Mono.error(new RuntimeException("Save failed")));

        Mono<DomainEvent> result = eventMongoAdapter.save(domainEvent);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage().equals("Save failed"))
                .verify();
    }
/*
    @Test
    void testFindAggregateSuccess() {
        when(repository.findByAggregateId("aggregateId")).thenReturn(Flux.just(eventEntity));
        when(mapper.readFromJson(any(String.class), any(Class.class))).thenReturn(domainEvent);

        Flux<DomainEvent> result = eventMongoAdapter.findAggregate("aggregateId");

        StepVerifier.create(result)
                .expectNextMatches(event -> event.getAggregateRootId().equals("aggregateId"))
                .verifyComplete();
    }
*/
    @Test
    void testFindAggregateNotFound() {
        when(repository.findByAggregateId("aggregateId")).thenReturn(Flux.empty());

        Flux<DomainEvent> result = eventMongoAdapter.findAggregate("aggregateId");

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }
/*
    @Test
    void testFindAllAggregatesSuccess() {
        when(repository.findAll()).thenReturn(Flux.just(eventEntity));
        when(mapper.readFromJson(any(String.class), any(Class.class))).thenAnswer(invocation -> {
            String json = invocation.getArgument(0);
            Class<?> clazz = invocation.getArgument(1);
            return new DomainEvent("TestEvent") {
                @Override
                public String getAggregateRootId() {
                    return "aggregateId";
                }
            };
        });

        Flux<DomainEvent> result = eventMongoAdapter.findAllAggregates();

        StepVerifier.create(result)
                .expectNextMatches(event -> event.getAggregateRootId().equals("aggregateId"))
                .verifyComplete();
    }*/
    @Test
    void testFindAllAggregatesNotFound() {
        when(repository.findAll()).thenReturn(Flux.empty());

        Flux<DomainEvent> result = eventMongoAdapter.findAllAggregates();

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }
}