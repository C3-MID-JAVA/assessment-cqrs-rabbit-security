package ec.com.sofka.appservice.gateway;

import ec.com.sofka.generics.domain.DomainEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

//Here you can create more functions according to the needs of the project related to events
public interface IEventStore {
    Mono<DomainEvent> save(DomainEvent event);
    Flux<DomainEvent> findAggregate(String aggregateId);
    Flux<DomainEvent> findAllAggregateByEvent(String eventType);
}

