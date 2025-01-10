package ec.com.sofka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.com.sofka.aggregate.events.AccountCreated;
import ec.com.sofka.aggregate.events.AccountUpdated;
import ec.com.sofka.aggregate.events.TransactionCreated;
import ec.com.sofka.applogs.gateway.BusMessageListener;
import ec.com.sofka.appservice.gateway.dto.AccountDTO;
import ec.com.sofka.appservice.gateway.dto.TransactionDTO;
import ec.com.sofka.appservice.queries.usecases.AccountSavedViewUseCase;
import ec.com.sofka.appservice.queries.usecases.AccountUpdatedViewUseCase;
import ec.com.sofka.appservice.queries.usecases.TransactionSavedViewUseCase;
import ec.com.sofka.enums.TransactionType;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.utils.AccountCreatedProperties;
import ec.com.sofka.utils.AccountUpdatedProperties;
import ec.com.sofka.utils.QueueManager;
import ec.com.sofka.utils.TransactionCreatedProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class BusListener implements BusMessageListener {

    private final AccountSavedViewUseCase accountSavedViewUseCase;
    private final AccountUpdatedViewUseCase accountUpdatedViewUseCase;
    private final TransactionSavedViewUseCase transactionSavedViewUseCase;
    private final AccountCreatedProperties accountCreatedProperties;
    private final AccountUpdatedProperties accountUpdatedProperties;
    private final TransactionCreatedProperties transactionCreatedProperties;



    public BusListener(
                       AccountSavedViewUseCase accountSavedViewUseCase,
                       AccountUpdatedViewUseCase accountUpdatedViewUseCase,
                       TransactionSavedViewUseCase transactionSavedViewUseCase,
                       AccountCreatedProperties accountCreatedProperties,
                       AccountUpdatedProperties accountUpdatedProperties,
                       TransactionCreatedProperties transactionCreatedProperties) {
        this.accountSavedViewUseCase = accountSavedViewUseCase;
        this.accountUpdatedViewUseCase = accountUpdatedViewUseCase;
        this.transactionSavedViewUseCase = transactionSavedViewUseCase;
        this.accountCreatedProperties = accountCreatedProperties;
        this.accountUpdatedProperties = accountUpdatedProperties;
        this.transactionCreatedProperties = transactionCreatedProperties;
    }

    @Override
    @RabbitListener(queues = "#{@accountCreatedProperties.getQueues()}")
    public void receiveAccountCreated(DomainEvent event) {
        AccountCreated accountCreated = (AccountCreated) event;
        AccountDTO accountDTO = new AccountDTO(
                accountCreated.getAccountId(),
                accountCreated.getName(),
                accountCreated.getAccountNumber(),
                accountCreated.getAccountBalance(),
                accountCreated.getStatus()
        );
        accountSavedViewUseCase.save(accountDTO);
    }

    @Override
    @RabbitListener(queues = "#{@accountUpdatedProperties.getQueues()}")
    public void receiveAccountUpdated(DomainEvent event) {
        AccountUpdated accountUpdated = (AccountUpdated) event;
        AccountDTO accountDTO = new AccountDTO(
                accountUpdated.getAccountId(),
                accountUpdated.getOwner(),
                accountUpdated.getAccountNumber(),
                accountUpdated.getBalance(),
                accountUpdated.getStatus()
        );
        accountUpdatedViewUseCase.update(accountDTO);
    }

    @Override
    @RabbitListener(queues = "#{@transactionCreatedProperties.getQueues()}")
    public void receiveTransactionCreated(DomainEvent event) {
        TransactionCreated transactionCreated = (TransactionCreated) event;
        TransactionDTO transactionDTO = new TransactionDTO(
                transactionCreated.getTransactionId(),
                transactionCreated.getAmount(),
                transactionCreated.getTransactionCost(),
                transactionCreated.getDate(),
                transactionCreated.getType(),
                transactionCreated.getAccountId()
        );

        transactionSavedViewUseCase.save(transactionDTO);
    }
}
