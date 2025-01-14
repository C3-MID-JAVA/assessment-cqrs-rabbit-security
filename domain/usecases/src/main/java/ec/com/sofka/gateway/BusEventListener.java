package ec.com.sofka.gateway;

import ec.com.sofka.generics.domain.DomainEvent;
import reactor.core.publisher.Mono;

public interface BusEventListener {

    void receiveAccountCreated(DomainEvent event);
    void receiveCardCreated(DomainEvent event);
    void receiveAccountUpdated(DomainEvent event);
    void receiveTransactionCreated(DomainEvent event);
}
