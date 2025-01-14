package ec.com.sofka;

import ec.com.sofka.account.queries.usecases.AccountSavedViewUseCase;
import ec.com.sofka.aggregate.customer.events.AccountBalanceUpdated;
import ec.com.sofka.aggregate.customer.events.AccountCreated;
import ec.com.sofka.aggregate.customer.events.UserCreated;
import ec.com.sofka.aggregate.operation.events.TransactionCreated;
import ec.com.sofka.gateway.BusEventListener;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.gateway.dto.TransactionDTO;
import ec.com.sofka.gateway.dto.UserDTO;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.transaction.TransactionType;
import ec.com.sofka.transaction.queries.usecases.TransactionSavedViewUseCase;
import ec.com.sofka.user.queries.usecases.UserSavedViewUseCase;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class BusListener implements BusEventListener {

    private final RabbitProperties rabbitProperties;
    private final AccountSavedViewUseCase accountSavedViewUseCase;
    private final UserSavedViewUseCase userSavedViewUseCase;
    private final TransactionSavedViewUseCase transactionSavedViewUseCase;

    public BusListener(
            RabbitProperties rabbitProperties,
            AccountSavedViewUseCase accountSavedViewUseCase,
            UserSavedViewUseCase userSavedViewUseCase,
            TransactionSavedViewUseCase transactionSavedViewUseCase
    ) {
        this.rabbitProperties = rabbitProperties;
        this.accountSavedViewUseCase = accountSavedViewUseCase;
        this.userSavedViewUseCase = userSavedViewUseCase;
        this.transactionSavedViewUseCase = transactionSavedViewUseCase;
    }

    @Override
    @RabbitListener(queues = "#{@rabbitProperties.getAccountQueue()}")
    public void receiveAccountCreated(DomainEvent event) {
        AccountCreated accountCreated = (AccountCreated) event;
        AccountDTO accountDTO = new AccountDTO(
                accountCreated.getId(),
                accountCreated.getAccountNumber(),
                accountCreated.getBalance(),
                accountCreated.getUserId()
        );
        accountSavedViewUseCase.accept(accountDTO);
    }

    @Override
    @RabbitListener(queues = "#{@rabbitProperties.getUserQueue()}")
    public void receiveUserCreated(DomainEvent event) {
        UserCreated userCreated = (UserCreated) event;
        UserDTO userDTO = new UserDTO(
                userCreated.getId(),
                userCreated.getName(),
                userCreated.getDocumentId()
        );
        userSavedViewUseCase.accept(userDTO);
    }

    @Override
    @RabbitListener(queues = "#{@rabbitProperties.getTransactionQueue()}")
    public void receiveTransactionCreated(DomainEvent event) {
        TransactionCreated transactionCreated = (TransactionCreated) event;
        TransactionDTO transactionDTO = new TransactionDTO(
                transactionCreated.getId(),
                transactionCreated.getAmount(),
                transactionCreated.getFee(),
                transactionCreated.getFinalAmount(),
                transactionCreated.getType(),
                transactionCreated.getTimestamp(),
                transactionCreated.getAccountId()
        );
        transactionSavedViewUseCase.accept(transactionDTO);
    }

    @Override
    @RabbitListener(queues = "#{@rabbitProperties.getAccountUpdatedQueue()}")
    public void receiveAccountUpdated(DomainEvent event) {
        AccountBalanceUpdated accountBalanceUpdated = (AccountBalanceUpdated) event;
        AccountDTO accountDTO = new AccountDTO(
                accountBalanceUpdated.getId(),
                accountBalanceUpdated.getAccountNumber(),
                accountBalanceUpdated.getBalance(),
                accountBalanceUpdated.getUserId()
        );
        accountSavedViewUseCase.accept(accountDTO);
    }
}
