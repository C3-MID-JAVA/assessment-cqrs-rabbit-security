package ec.com.sofka.appservice.gateway;

import ec.com.sofka.generics.domain.DomainEvent;
import reactor.core.publisher.Mono;

//7. New gateway to establish the link for the outside just as we do with repository
public interface IBusEvent {
    void sendEventAccountCreated(Mono<DomainEvent> event);
    void sendEventAccountUpdated(Mono<DomainEvent> event);
    void sendEventTransactionCreated(Mono<DomainEvent> event);
}
