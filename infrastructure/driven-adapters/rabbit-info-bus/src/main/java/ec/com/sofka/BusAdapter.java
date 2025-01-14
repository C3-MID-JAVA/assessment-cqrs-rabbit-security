package ec.com.sofka;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.generics.domain.DomainEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;


@Service
public class BusAdapter implements BusEvent {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitEnvProperties envProperties;

    public BusAdapter(RabbitTemplate rabbitTemplate, RabbitEnvProperties envProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.envProperties = envProperties;
    }

    @Override
    public void sendEventAccountCreated(Mono<DomainEvent> event) {
        event.subscribe(domainEvent -> {
                    rabbitTemplate.convertAndSend(envProperties.getAccountExchange(),
                            envProperties.getAccountRoutingKey(), domainEvent);
                }
        );
    }

    @Override
    public void sendEventAccountUpdated(Mono<DomainEvent> event) {
        event.subscribe(domainEvent -> {
                    rabbitTemplate.convertAndSend(envProperties.getAccountUpdatedExchange(),
                            envProperties.getAccountUpdateRoutingKey(), domainEvent);
                }
        );
    }

    @Override
    public void sendEventCardCreated(Mono<DomainEvent> event) {
        event.subscribe(domainEvent -> {
                    rabbitTemplate.convertAndSend(envProperties.getCardExchange(),
                            envProperties.getCardRoutingKey(), domainEvent);
                }
        );

    }

    @Override
    public void sendEventTransactionCreated(Mono<DomainEvent> event) {
        event.subscribe(domainEvent -> {
           rabbitTemplate.convertAndSend(envProperties.getTransactionExchange(),
                   envProperties.getTransactionRoutingKey(), domainEvent);
        });
    }
}
