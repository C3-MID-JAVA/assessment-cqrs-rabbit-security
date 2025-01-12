/*package ec.com.sofka;

import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.generics.domain.DomainEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


//11. BusMessage implementation, this is a service so, don't forget the annotation
@Service
public class BusAdapter implements BusEvent {

    //13. Use of RabbitTemplate to define the sendMsg method
    private final RabbitTemplate rabbitTemplate;

    public BusAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendEvent(DomainEvent event) {
        rabbitTemplate.convertAndSend("account.exchange",
                "account.routingKey",
                event);
    }

}

 */
/*
package ec.com.sofka;

import ec.com.sofka.aggregate.events.AccountCreated;
import ec.com.sofka.aggregate.events.AccountUpdated;
import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.generics.domain.DomainEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BusAdapter implements BusEvent {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.account.created.exchange}")
    private String accountCreatedExchange;

    @Value("${app.account.created.routingKey}")
    private String accountCreatedRoutingKey;

    @Value("${app.account.updated.exchange}")
    private String accountUpdatedExchange;

    @Value("${app.account.updated.routingKey}")
    private String accountUpdatedRoutingKey;

    public BusAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendEvent(DomainEvent event) {
        String exchange;
        String routingKey;

        if (event instanceof AccountCreated) {
            exchange = accountCreatedExchange;
            routingKey = accountCreatedRoutingKey;
        } else if (event instanceof AccountUpdated) {
            exchange = accountUpdatedExchange;
            routingKey = accountUpdatedRoutingKey;
        } else {
            throw new IllegalArgumentException("Unsupported event type");
        }

        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}
*/
package ec.com.sofka;

import ec.com.sofka.aggregate.events.AccountCreated;
import ec.com.sofka.aggregate.events.AccountUpdated;
import ec.com.sofka.aggregate.events.TransactionCreated;
import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.generics.domain.DomainEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BusAdapter implements BusEvent {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.account.created.exchange}")
    private String accountCreatedExchange;

    @Value("${app.account.created.routingKey}")
    private String accountCreatedRoutingKey;

    @Value("${app.account.updated.exchange}")
    private String accountUpdatedExchange;

    @Value("${app.account.updated.routingKey}")
    private String accountUpdatedRoutingKey;

    @Value("${app.transaction.created.exchange}")
    private String transactionCreatedExchange;

    @Value("${app.transaction.created.routingKey}")
    private String transactionCreatedRoutingKey;

    public BusAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendEvent(DomainEvent event) {
        String exchange;
        String routingKey;

        if (event instanceof AccountCreated) {
            exchange = accountCreatedExchange;
            routingKey = accountCreatedRoutingKey;
        } else if (event instanceof AccountUpdated) {
            exchange = accountUpdatedExchange;
            routingKey = accountUpdatedRoutingKey;
        } else if (event instanceof TransactionCreated) {
            exchange = transactionCreatedExchange;
            routingKey = transactionCreatedRoutingKey;
        } else {
            throw new IllegalArgumentException("Unsupported event type");
        }

        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}
