package ec.com.sofka.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:rabbit-application.properties")
public class AccountUpdatedProperties {

    @Value("${account.exchange.name}")
    private String exchangeName;

    @Value("${account.queue.updated}")
    private String queueName;

    @Value("${account.routing.key.updated}")
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
