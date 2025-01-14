package ec.com.sofka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:rabbit-configuration.properties")
public class RabbitProperties {

    @Value("${user.created.exchange}")
    private String userExchange;

    @Value("${user.created.queue}")
    private String userQueue;

    @Value("${user.created.routingKey}")
    private String userRoutingKey;

    @Value("${account.created.exchange}")
    private String accountCreatedExchange;

    @Value("${account.created.queue}")
    private String accountCreatedQueue;

    @Value("${account.created.routingKey}")
    private String accountCreatedRoutingKey;

    @Value("${account.updated.exchange}")
    private String accountUpdatedExchange;

    @Value("${account.updated.queue}")
    private String accountUpdatedQueue;

    @Value("${account.updated.routingKey}")
    private String accountUpdatedRoutingKey;

    @Value("${transaction.created.exchange}")
    private String transactionCreatedExchange;

    @Value("${transaction.created.queue}")
    private String transactionCreatedQueue;

    @Value("${transaction.created.routingKey}")
    private String transactionCreatedRoutingKey;

    public String getUserExchange() {
        return userExchange;
    }

    public String getUserQueue() {
        return userQueue;
    }

    public String getUserRoutingKey() {
        return userRoutingKey;
    }

    public String getAccountExchange() {
        return accountCreatedExchange;
    }

    public String getAccountQueue() {
        return accountCreatedQueue;
    }

    public String getAccountRoutingKey() {
        return accountCreatedRoutingKey;
    }

    public String getTransactionExchange() {
        return transactionCreatedExchange;
    }

    public String getTransactionQueue() {
        return transactionCreatedQueue;
    }

    public String getTransactionRoutingKey() {
        return transactionCreatedRoutingKey;
    }

    public String getAccountUpdatedExchange() {
        return accountUpdatedExchange;
    }

    public String getAccountUpdatedQueue() {
        return accountUpdatedQueue;
    }

    public String getAccountUpdatedRoutingKey() {
        return accountUpdatedRoutingKey;
    }
}

