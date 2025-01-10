package ec.com.sofka.adapter;

import ec.com.sofka.JSONMap;
import ec.com.sofka.data.EventEntity;
import ec.com.sofka.database.events.IEventMongoRepository;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@Repository
public class EventMongoAdapter implements IEventStore {

    private final IEventMongoRepository repository;
    private final JSONMap mapper;

    public EventMongoAdapter(IEventMongoRepository repository, JSONMap mapper) {
        this.repository = repository;
        this.mapper = mapper;
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
        return repository.save(e)
                .thenReturn(event);
    }

    @Override
    public Flux<DomainEvent> findAggregate(String aggregateId, String aggregate) {
        return repository.findAllByAggregateId(aggregateId)
                .map(eventEntity -> eventEntity.deserializeEvent(mapper, aggregate))
                .sort(Comparator.comparing(DomainEvent::getVersion)
                        .thenComparing(DomainEvent::getWhen));
    }

    @Override
    public Flux<DomainEvent> findAllAggregate(String aggregate) {
        return repository.findAllByAggregateRootName(aggregate)
                .map(eventEntity -> eventEntity.deserializeEvent(mapper, aggregate))
                .sort(Comparator.comparing(DomainEvent::getVersion)
                        .thenComparing(DomainEvent::getWhen));
    }
}
