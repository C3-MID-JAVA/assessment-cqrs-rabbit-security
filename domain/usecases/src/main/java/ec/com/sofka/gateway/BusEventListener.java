package ec.com.sofka.gateway;

import ec.com.sofka.generics.domain.DomainEvent;

public interface BusEventListener {
    void receiveAccountCreated(DomainEvent event);
    void receiveUserCreated(DomainEvent event);
    void receiveTransactionCreated(DomainEvent event);
    void receiveAccountUpdated(DomainEvent event);
}
