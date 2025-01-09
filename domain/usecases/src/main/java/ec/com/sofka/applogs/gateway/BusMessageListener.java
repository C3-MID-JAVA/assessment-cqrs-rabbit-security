package ec.com.sofka.applogs.gateway;

import ec.com.sofka.generics.domain.DomainEvent;

//18. Port for listening messages
public interface BusMessageListener {
    void receiveAccountCreated(DomainEvent event);
    void receiveAccountUpdated(DomainEvent event);
    void receiveTransactionCreated(DomainEvent event);
}

