package ec.com.sofka;

import ec.com.sofka.account.queries.AccountSavedViewUseCase;
import ec.com.sofka.account.queries.TransactionSavedViewUseCase;
import ec.com.sofka.account.queries.UserSavedViewUseCase;
import ec.com.sofka.aggregate.customer.events.AccountCreated;
import ec.com.sofka.aggregate.customer.events.UserCreated;
import ec.com.sofka.aggregate.operation.events.TransactionCreated;
import ec.com.sofka.gateway.BusEventListener;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.gateway.dto.TransactionDTO;
import ec.com.sofka.gateway.dto.UserDTO;
import ec.com.sofka.generics.domain.DomainEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class BusListener implements BusEventListener {

    private final AccountSavedViewUseCase accountSavedViewUseCase;
    private final UserSavedViewUseCase userSavedViewUseCase;
    private final TransactionSavedViewUseCase transactionViewCase;

    public BusListener(
            AccountSavedViewUseCase accountSavedViewUseCase,
            UserSavedViewUseCase userSavedViewUseCase,
            TransactionSavedViewUseCase transactionViewUseCase
    ) {
        this.transactionViewCase = transactionViewUseCase;
        this.userSavedViewUseCase = userSavedViewUseCase;
        this.accountSavedViewUseCase = accountSavedViewUseCase;
    }

    @Override
    @RabbitListener(queues = "account.created.queue")
    public void receiveAccountCreated(DomainEvent event) {
        AccountCreated accountCreated = (AccountCreated) event;
        AccountDTO accountDTO = new AccountDTO(
                accountCreated.getId(),
                accountCreated.getAccountNumber(),
                accountCreated.getBalance(),
                accountCreated.getUserId()
        );
        System.out.println(accountCreated.getId());
        accountSavedViewUseCase.accept(accountDTO);
    }

    @Override
    @RabbitListener(queues = "user.created.queue") // Vincular a la cola de RabbitMQ
    public void receiveUserCreated(DomainEvent event) {
        UserCreated userCreated = (UserCreated) event;
        UserDTO userDTO = new UserDTO(
                userCreated.getId(),
                userCreated.getName(),
                userCreated.getDocumentId()
        );
        System.out.println(userCreated.getId());
        userSavedViewUseCase.accept(userDTO); // Procesar el evento
    }

    @RabbitListener(queues = "transaction.created.queue")
    public void receiveTransactionCreated(DomainEvent event) {
        TransactionCreated transactionCreated = (TransactionCreated) event;
        TransactionDTO transactionDTO = new TransactionDTO(
                transactionCreated.getAmount(),
                transactionCreated.getFee(),
                transactionCreated.getNetAmount(),
                transactionCreated.getType(),
                transactionCreated.getTimestamp(),
                transactionCreated.getAccountId()
        );
        System.out.println(transactionCreated.getId());
        transactionViewCase.accept(transactionDTO);
    }

}
