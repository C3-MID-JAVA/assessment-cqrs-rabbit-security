package ec.com.sofka.utils;

import org.springframework.stereotype.Component;

@Component
public class QueueManager {

    private final AccountCreatedProperties accountProperties;
    private final TransactionCreatedProperties transactionProperties;

    public QueueManager(AccountCreatedProperties accountProperties, TransactionCreatedProperties transactionProperties) {
        this.accountProperties = accountProperties;
        this.transactionProperties = transactionProperties;
    }

    public String[] getAllQueues() {
        return new String[] {
                accountProperties.getQueueName(),
                transactionProperties.getQueueName()
        };
    }
}
