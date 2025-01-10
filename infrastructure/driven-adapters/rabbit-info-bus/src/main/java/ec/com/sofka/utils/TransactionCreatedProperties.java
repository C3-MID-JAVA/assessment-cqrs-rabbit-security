package ec.com.sofka.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:rabbit-application.properties")
public class TransactionCreatedProperties {

    @Value("${transaction.exchange.created.name}")
    private String exchangeName;

    @Value("${transaction.queue.created.name}")
    private String queueName;

    @Value("${transaction.routing.key.created}")
    private String routingKey;

    public String getExchangeName() {
        return exchangeName;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public String[] getQueues() {
        return new String[] {
                getQueueName(),
        };
    }
}
