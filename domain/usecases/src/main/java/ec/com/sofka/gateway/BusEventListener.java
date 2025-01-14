package ec.com.sofka.gateway;

import ec.com.sofka.generics.domain.DomainEvent;

public interface BusEventListener {
    void receiveAccountCreated(DomainEvent event);
    void receiveAccountUpdated(DomainEvent event);
    void receiveTransactionCreated(DomainEvent event);
}
