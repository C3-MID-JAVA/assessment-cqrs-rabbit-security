/*package ec.com.sofka;

import ec.com.sofka.data.EventEntity;
import ec.com.sofka.database.events.IEventMongoRepository;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;

@Repository
public class EventMongoAdapter implements IEventStore {

    private final IEventMongoRepository repository;
    private final JSONMap mapper;
   // private final MongoTemplate eventMongoTemplate;

    public EventMongoAdapter(IEventMongoRepository repository, JSONMap mapper) {
        this.repository = repository;
        this.mapper = mapper;
       // this.eventMongoTemplate = eventMongoTemplate;
    }

    @Override
    public DomainEvent save(DomainEvent event) {
        EventEntity e = new EventEntity(
                event.getEventId(),
                event.getAggregateRootId(),
                event.getEventType(),
                EventEntity.wrapEvent(event, mapper),
                event.getWhen().toString(),
                event.getVersion()

        );
        repository.save(e);
        return event;
    }

    @Override
    public List<DomainEvent> findAggregate(String aggregateId) {
        List<EventEntity> entities = repository.findByAggregateId(aggregateId);
        return entities.stream()
                .map(eventEntity -> eventEntity.deserializeEvent(mapper))
                .sorted(Comparator.comparing(DomainEvent::getVersion))
                .toList();
    }

    @Override
    public List<DomainEvent> findAllAggregates() {
        return repository.findAll().stream()
                .map(eventEntity ->eventEntity.deserializeEvent(mapper))
                .sorted(Comparator.comparing(DomainEvent::getAggregateRootId)
                        .thenComparing(DomainEvent::getVersion, Comparator.reverseOrder()))
                .toList();
    }
}

 */

package ec.com.sofka;

import ec.com.sofka.data.EventEntity;
import ec.com.sofka.database.events.IEventMongoRepository;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

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
                event.getVersion()
        );
        return repository.save(e).thenReturn(event);
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
