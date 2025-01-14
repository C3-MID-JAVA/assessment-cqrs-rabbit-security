package ec.com.sofka;

import ec.com.sofka.data.EventEntity;
import ec.com.sofka.repository.events.IEventMongoRepository;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@Repository
public class EventMongoAdapter implements IEventStore {
    private final IEventMongoRepository repository;
    private final JSONMap mapper;
    private final ReactiveMongoTemplate eventReactiveMongoTemplate;

    public EventMongoAdapter(IEventMongoRepository repository, JSONMap mapper, @Qualifier("eventReactiveMongoTemplate")ReactiveMongoTemplate eventReactiveMongoTemplate) {
        this.repository = repository;
        this.mapper = mapper;
        this.eventReactiveMongoTemplate = eventReactiveMongoTemplate;
    }

    @Override
    public Mono<DomainEvent> save(DomainEvent event) {
        return Mono.just(event)
                .map(evt -> {
                    String eventData = mapper.writeToJson(evt);
                    return new EventEntity(
                            evt.getEventId(),
                            evt.getAggregateRootId(),
                            evt.getEventType(),
                            eventData,
                            evt.getWhen().toString(),
                            evt.getVersion()
                    );
                })
                .flatMap(repository::save)
                .thenReturn(event);
    }

    @Override
    public Flux<DomainEvent> findAggregate(String aggregateId) {
        return repository.findByAggregateId(aggregateId)
                .map(eventEntity -> eventEntity.deserializeEvent(mapper))
                .sort(Comparator.comparing(DomainEvent::getVersion));
    }

    @Override
    public Flux<DomainEvent> findAllAggregates() {
        return repository.findAll()
                .map(eventEntity -> eventEntity.deserializeEvent(mapper))
                .sort(Comparator.comparing(DomainEvent::getAggregateRootId)
                        .thenComparing(DomainEvent::getVersion, Comparator.reverseOrder()));
    }

}