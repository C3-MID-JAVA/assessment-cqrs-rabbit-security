package ec.com.sofka.gateway;

import ec.com.sofka.generics.domain.DomainEvent;
import reactor.core.publisher.Mono;

public interface BusEvent {
    void sendEventAccountCreated(Mono<DomainEvent> event);
    void sendEventAccountUpdated(Mono<DomainEvent> event);
    void sendEventCardCreated(Mono<DomainEvent> event);
    void sendEventTransactionCreated(Mono<DomainEvent> event);
}
