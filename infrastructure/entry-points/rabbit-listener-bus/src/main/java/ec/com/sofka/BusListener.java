/*package ec.com.sofka;
import ec.com.sofka.aggregate.events.AccountCreated;
import ec.com.sofka.aggregate.events.AccountUpdated;
import ec.com.sofka.gateway.BusEventListener;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.queries.usecases.AccountSavedViewUseCase;
import ec.com.sofka.queries.usecases.TransactionSavedViewUseCase;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

//20. Create the BusListener class
@Component
@Service
public class BusListener implements BusEventListener {

    @Value("${app.account.created.queue}")
    private String accountCreatedQueue;

    @Value("${app.account.updated.queue}")
    private String accountUpdatedQueue;

    @Value("${app.transaction.created.queue}")
    private String transactionCreatedQueue;

    private final AccountSavedViewUseCase accountSavedViewUseCase;
    private final TransactionSavedViewUseCase transactionSavedViewUseCase;

    public BusListener(AccountSavedViewUseCase accountSavedViewUseCase, TransactionSavedViewUseCase transactionSavedViewUseCase) {
        this.accountSavedViewUseCase = accountSavedViewUseCase;
        this.transactionSavedViewUseCase = transactionSavedViewUseCase;
    }

    @Override
    @RabbitListener(queues = "${app.account.created.queue}")
    public void receiveAccountCreated(DomainEvent event) {
        AccountCreated accountEvent = (AccountCreated) event;
        accountSavedViewUseCase.accept(new AccountDTO(
                accountEvent.getAccountId(),
                accountEvent.getName(),
                accountEvent.getAccountNumber(),
                accountEvent.getAccountBalance(),
                accountEvent.getStatus(),
                accountEvent.getUserId()
        ));

    }

    @Override
    @RabbitListener(queues = "${app.account.updated.queue}")
    public void receiveAccountUpdated(DomainEvent event) {
        AccountUpdated accountEvent = (AccountUpdated) event;
        accountSavedViewUseCase.accept(new AccountDTO(
                accountEvent.getAccountId(),
                accountEvent.getName(),
                accountEvent.getAccountNumber(),
                accountEvent.getBalance(),
                accountEvent.getStatus(),
                accountEvent.getUserId()));

    }

}
*/

package ec.com.sofka;

import ec.com.sofka.aggregate.events.AccountCreated;
import ec.com.sofka.aggregate.events.AccountUpdated;
import ec.com.sofka.aggregate.events.TransactionCreated;
import ec.com.sofka.gateway.BusEventListener;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.gateway.dto.TransactionDTO;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.queries.usecases.AccountSavedViewUseCase;
import ec.com.sofka.queries.usecases.TransactionSavedViewUseCase;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class BusListener implements BusEventListener {

    @Value("${app.account.created.queue}")
    private String accountCreatedQueue;

    @Value("${app.account.updated.queue}")
    private String accountUpdatedQueue;

    @Value("${app.transaction.created.queue}")
    private String transactionCreatedQueue;

    private final AccountSavedViewUseCase accountSavedViewUseCase;
    private final TransactionSavedViewUseCase transactionSavedViewUseCase;

    public BusListener(AccountSavedViewUseCase accountSavedViewUseCase, TransactionSavedViewUseCase transactionSavedViewUseCase) {
        this.accountSavedViewUseCase = accountSavedViewUseCase;
        this.transactionSavedViewUseCase = transactionSavedViewUseCase;
    }

    @Override
    @RabbitListener(queues = "${app.account.created.queue}")
    public void receiveAccountCreated(DomainEvent event) {
        AccountCreated accountEvent = (AccountCreated) event;
        accountSavedViewUseCase.accept(new AccountDTO(
                accountEvent.getAccountId(),
                accountEvent.getName(),
                accountEvent.getAccountNumber(),
                accountEvent.getAccountBalance(),
                accountEvent.getStatus(),
                accountEvent.getUserId()
        ));
    }

    @Override
    @RabbitListener(queues = "${app.account.updated.queue}")
    public void receiveAccountUpdated(DomainEvent event) {
        AccountUpdated accountEvent = (AccountUpdated) event;
        accountSavedViewUseCase.accept(new AccountDTO(
                accountEvent.getAccountId(),
                accountEvent.getName(),
                accountEvent.getAccountNumber(),
                accountEvent.getBalance(),
                accountEvent.getStatus(),
                accountEvent.getUserId()
        ));
    }

    @RabbitListener(queues = "${app.transaction.created.queue}")
    public void receiveTransactionCreated(DomainEvent event) {
        TransactionCreated transactionEvent = (TransactionCreated) event;
        transactionSavedViewUseCase.accept(new TransactionDTO(
                transactionEvent.getTransactionId(),
                transactionEvent.getAmount(),
                transactionEvent.getType(),
                transactionEvent.getCost(),
                transactionEvent.getIdAccount(),
                transactionEvent.getStatus()
        ));
    }
}