package ec.com.sofka;

import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.generics.domain.DomainEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BusAdapter implements BusEvent {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitProperties rabbitProperties;

    public BusAdapter(RabbitTemplate rabbitTemplate, RabbitProperties rabbitProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitProperties = rabbitProperties;
    }

    @Override
    public void sendEventAccountCreated(Mono<DomainEvent> event) {
        event.subscribe(accountCreated -> {
                    rabbitTemplate.convertAndSend(rabbitProperties.getAccountExchange(), rabbitProperties.getAccountRoutingKey(), accountCreated);
                }
        );
    }

    @Override
    public void sendEventUserCreated(Mono<DomainEvent> event) {
        event.subscribe(userCreated -> {
                    rabbitTemplate.convertAndSend(rabbitProperties.getUserExchange(), rabbitProperties.getUserRoutingKey(), userCreated);
                }
        );
    }

    @Override
    public void sendEventTransactionCreated(Mono<DomainEvent> event) {
        event.subscribe(transactionCreated -> {
            rabbitTemplate.convertAndSend(rabbitProperties.getTransactionExchange(), rabbitProperties.getTransactionRoutingKey(), transactionCreated);
        });
    }

    @Override
    public void sendEventAccountUpdated(Mono<DomainEvent> event) {
        event.subscribe(accountUpdated -> {
                    rabbitTemplate.convertAndSend(rabbitProperties.getAccountUpdatedExchange(), rabbitProperties.getAccountUpdatedRoutingKey(), accountUpdated);
                }
        );
    }
}
