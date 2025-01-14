package ec.com.sofka.adapters;

import ec.com.sofka.IJSONMapper;
import ec.com.sofka.JSONMap;
import ec.com.sofka.data.EventEntity;
import ec.com.sofka.database.events.IEventMongoRepository;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@Repository
public class EventMongoAdapter implements IEventStore {

    private final IEventMongoRepository repository;
    private final JSONMap mapper;
    private final ReactiveMongoTemplate eventMongoTemplate;

    public EventMongoAdapter(IEventMongoRepository repository, JSONMap mapper, @Qualifier("eventMongoTemplate")ReactiveMongoTemplate eventMongoTemplate) {
        this.repository = repository;
        this.mapper = mapper;
        this.eventMongoTemplate = eventMongoTemplate;
    }

    @Override
    public Mono<DomainEvent> save(DomainEvent event) {
        EventEntity e = new EventEntity(
                event.getEventId(),
                event.getAggregateRootId(),
                event.getEventType(),
                EventEntity.wrapEvent(event, mapper),
                event.getWhen().toString(),
                event.getVersion(),
                event.getAggregateRootName()

        );
        return repository.save(e).flatMap( ev->Mono.just(event));
    }

    @Override
    public Flux<DomainEvent> findAggregate(String aggregateId) {
        return repository.findAllByAggregateId(aggregateId)
                .map(eventEntity -> eventEntity.deserializeEvent(mapper))
                .sort(Comparator.comparing(DomainEvent::getVersion)
                        .thenComparing(DomainEvent::getWhen));
    }

    @Override
    public Flux<DomainEvent> findAllAggregate(String aggregate) {
        return repository.findAllByAggregateRootName(aggregate)
                .map(eventEntity -> eventEntity.deserializeEvent(mapper))
                .sort(Comparator.comparing(DomainEvent::getVersion)
                        .thenComparing(DomainEvent::getWhen));
    }
}
