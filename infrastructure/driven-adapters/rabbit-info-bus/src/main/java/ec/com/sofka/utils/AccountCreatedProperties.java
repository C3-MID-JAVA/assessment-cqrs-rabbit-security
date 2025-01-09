package ec.com.sofka.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:rabbit-application.properties")
public class AccountCreatedProperties {

    @Value("${account.exchange.name}")
    private String exchangeName;

    @Value("${account.queue.created.name}")
    private String queueName;

    @Value("${account.routing.key.created}")
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
