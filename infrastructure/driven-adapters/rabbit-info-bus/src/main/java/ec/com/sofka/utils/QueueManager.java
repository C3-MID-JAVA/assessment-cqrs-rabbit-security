package ec.com.sofka.utils;

import org.springframework.stereotype.Component;

@Component
public class QueueManager {

    private final AccountCreatedProperties accountProperties;
    private final AccountUpdatedProperties accountUpdatedProperties;
    private final TransactionCreatedProperties transactionProperties;

    public QueueManager(AccountCreatedProperties accountProperties, AccountUpdatedProperties accountUpdatedProperties, TransactionCreatedProperties transactionProperties) {
        this.accountProperties = accountProperties;
        this.accountUpdatedProperties = accountUpdatedProperties;
        this.transactionProperties = transactionProperties;
    }

    public String[] getAllQueues() {
        return new String[] {
                accountProperties.getQueueName(),
                transactionProperties.getQueueName(),
                accountUpdatedProperties.getQueueName()
        };
    }
}
