package ec.com.sofka;

import ec.com.sofka.gateway.busMessage.EventBusMessage;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.utils.enums.EventsDetailsEnum;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class EventsBusAdapter implements EventBusMessage {
    private final RabbitTemplate rabbitTemplate;
    private final Environment environment;

    public EventsBusAdapter(RabbitTemplate rabbitTemplate, Environment environment) {
        this.rabbitTemplate = rabbitTemplate;
        this.environment = environment;
    }

    @Override
    public void sendEvent(DomainEvent event) {
        EventsDetailsEnum eventsDetails = EventsDetailsEnum.findByEventType(event.getEventType());

        rabbitTemplate.convertAndSend(Objects.requireNonNull(environment.getProperty(eventsDetails.getExchange())),
                Objects.requireNonNull(environment.getProperty(eventsDetails.getRoutingKey())),
                event);
    }
}