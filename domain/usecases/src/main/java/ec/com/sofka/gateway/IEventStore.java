package ec.com.sofka.gateway;

import ec.com.sofka.generics.domain.DomainEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IEventStore {
    Mono<DomainEvent> save(DomainEvent event);

    Flux<DomainEvent> findAggregate(String aggregateId, String aggregate);

    Flux<DomainEvent> findAllAggregate(String aggregate);

    Flux<DomainEvent> findAllAggregateByEvent(String aggregate, String eventType);
}