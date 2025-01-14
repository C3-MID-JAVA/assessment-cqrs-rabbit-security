package ec.com.sofka;

import ec.com.sofka.appservice.gateway.IBusEvent;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.utils.AccountCreatedProperties;
import ec.com.sofka.utils.AccountUpdatedProperties;
import ec.com.sofka.utils.TransactionCreatedProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BusAdapter implements IBusEvent {

    private final RabbitTemplate rabbitTemplate;
    private final AccountCreatedProperties accountCreatedProperties;
    private final TransactionCreatedProperties transactionCreatedProperties;
    private final AccountUpdatedProperties accountUpdatedProperties;

    public BusAdapter(RabbitTemplate rabbitTemplate, AccountCreatedProperties accountCreatedProperties,
                      TransactionCreatedProperties transactionCreatedProperties,
                      AccountUpdatedProperties accountUpdatedProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.accountCreatedProperties = accountCreatedProperties;
        this.transactionCreatedProperties = transactionCreatedProperties;
        this.accountUpdatedProperties = accountUpdatedProperties;
    }

    @Override
    public void sendEventAccountCreated(Mono<DomainEvent> event) {
        event.subscribe(account -> {
            rabbitTemplate.convertAndSend(accountCreatedProperties.getExchangeName(), accountCreatedProperties.getRoutingKey(), account);
        });
    }

    @Override
    public void sendEventAccountUpdated(Mono<DomainEvent> event) {
        event.subscribe(account -> {
            rabbitTemplate.convertAndSend(accountUpdatedProperties.getExchangeName(), accountUpdatedProperties.getRoutingKey(), account);
        });
    }

    @Override
    public void sendEventTransactionCreated(Mono<DomainEvent> event) {
        event.subscribe(transaction -> {
            rabbitTemplate.convertAndSend(transactionCreatedProperties.getExchangeName(), transactionCreatedProperties.getRoutingKey(), transaction);
        });
    }
}
