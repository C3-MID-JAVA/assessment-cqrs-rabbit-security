package ec.com.sofka;

import ec.com.sofka.aggregates.account.events.AccountCreated;
import ec.com.sofka.aggregates.account.events.AccountUpdated;
import ec.com.sofka.aggregates.account.events.TransactionCreated;
import ec.com.sofka.aggregates.account.events.TransactionTypeCreated;
import ec.com.sofka.aggregates.account.events.CustomerCreated;
import ec.com.sofka.aggregates.auth.events.UserCreated;
import ec.com.sofka.aggregates.auth.events.UserUpdated;
import ec.com.sofka.gateway.busMessage.BusEventListener;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.mapper.*;
import ec.com.sofka.queries.usecases.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class EventBusListener implements BusEventListener {
    private final UserSavedViewUseCase userSavedViewUseCase;
    private final CustomerSavedViewUseCase customerSavedViewUseCase;
    private final AccountSavedViewUseCase accountSavedViewUseCase;
    private final TransactionTypeSavedViewUseCase transactionTypeSavedViewUseCase;
    private final TransactionSavedViewUseCase transactionSavedViewUseCase;

    public EventBusListener(UserSavedViewUseCase userSavedViewUseCase, CustomerSavedViewUseCase customerSavedViewUseCase, AccountSavedViewUseCase accountSavedViewUseCase, TransactionTypeSavedViewUseCase transactionTypeSavedViewUseCase, TransactionSavedViewUseCase transactionSavedViewUseCase) {
        this.userSavedViewUseCase = userSavedViewUseCase;
        this.customerSavedViewUseCase = customerSavedViewUseCase;
        this.accountSavedViewUseCase = accountSavedViewUseCase;
        this.transactionTypeSavedViewUseCase = transactionTypeSavedViewUseCase;
        this.transactionSavedViewUseCase = transactionSavedViewUseCase;
    }

    @Override
    @RabbitListener(queues = "${user.created.queue}")
    public void receiveUserCreate(DomainEvent event) {
        UserCreated userCreated = (UserCreated) event;
        userSavedViewUseCase.accept(UserMapper.mapToDTOFromCreatedEvent(userCreated));

    }

    @Override
    @RabbitListener(queues = "${user.updated.queue}")
    public void receiveUserUpdated(DomainEvent event) {
        UserUpdated userUpdated = (UserUpdated) event;
        userSavedViewUseCase.accept(UserMapper.mapToDTOFromUpdatesEvent(userUpdated));

    }

    @Override
    @RabbitListener(queues = "${customer.created.queue}")
    public void receiveCustomerCreate(DomainEvent event) {
        CustomerCreated customerEvent = (CustomerCreated) event;
        customerSavedViewUseCase.accept(CustomerMapper.mapToDTOFromEvent(customerEvent));

    }

    @Override
    @RabbitListener(queues = "${account.created.queue}")
    public void receiveAccountCreate(DomainEvent event) {
        AccountCreated accountEvent = (AccountCreated) event;
        accountSavedViewUseCase.accept(AccountMapper.mapToDTOFromCreatedEvent(accountEvent));

    }

    @Override
    @RabbitListener(queues = "${account.updated.queue}")
    public void receiveAccountUpdate(DomainEvent event) {
        AccountUpdated accountEvent = (AccountUpdated) event;
        accountSavedViewUseCase.accept(AccountMapper.mapToDTOFromUpdatesEvent(accountEvent));

    }

    @Override
    @RabbitListener(queues = "${transactionType.created.queue}")
    public void receiveTransactionTypeCreate(DomainEvent event) {
        TransactionTypeCreated transactionTypeEvent = (TransactionTypeCreated) event;
        transactionTypeSavedViewUseCase.accept(TransactionTypeMapper.mapToDTOFromEvent(transactionTypeEvent));

    }

    @Override
    @RabbitListener(queues = "${transaction.created.queue}")
    public void receiveTransactionCreate(DomainEvent event) {
        TransactionCreated transactionEvent = (TransactionCreated) event;
        transactionSavedViewUseCase.accept(TransactionMapper.mapToDTOFromEvent(transactionEvent));

    }

}