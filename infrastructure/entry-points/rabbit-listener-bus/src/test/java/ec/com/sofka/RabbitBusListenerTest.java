package ec.com.sofka;

import ec.com.sofka.account.queries.usecases.AccountSavedViewUseCase;
import ec.com.sofka.aggregate.customer.events.AccountBalanceUpdated;
import ec.com.sofka.aggregate.customer.events.AccountCreated;
import ec.com.sofka.aggregate.customer.events.UserCreated;
import ec.com.sofka.aggregate.operation.events.TransactionCreated;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.gateway.dto.UserDTO;
import ec.com.sofka.transaction.TransactionType;
import ec.com.sofka.transaction.queries.usecases.TransactionSavedViewUseCase;
import ec.com.sofka.user.queries.usecases.UserSavedViewUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RabbitBusListenerTest {

    @Mock
    private AccountSavedViewUseCase accountSavedViewUseCase;

    @Mock
    private UserSavedViewUseCase userSavedViewUseCase;

    @Mock
    private TransactionSavedViewUseCase transactionSavedViewUseCase;

    @InjectMocks
    private BusListener listener;

    @Test
    void shouldProcessAccountCreatedEvent() {
        AccountCreated event = new AccountCreated(
                "accountId",
                "123456789",
                BigDecimal.valueOf(1000.0),
                "userId"
        );

        AccountDTO expectedDTO = new AccountDTO(
                "accountId",
                "123456789",
                BigDecimal.valueOf(1000.0),
                "userId"
        );

        listener.receiveAccountCreated(event);

        verify(accountSavedViewUseCase).accept(argThat(accountDTO ->
                accountDTO.getId().equals("accountId") &&
                        accountDTO.getAccountNumber().equals("123456789") &&
                        Objects.equals(accountDTO.getBalance(), BigDecimal.valueOf(1000.0)) &&
                        accountDTO.getUserId().equals("userId")
        ));
    }

    @Test
    void shouldProcessUserCreatedEvent() {
        UserCreated event = new UserCreated(
                "userId",
                "Test User",
                "1234567890"
        );

        UserDTO expectedDTO = new UserDTO(
                "userId",
                "Test User",
                "1234567890"
        );

        listener.receiveUserCreated(event);

        verify(userSavedViewUseCase).accept(argThat(userDTO ->
                userDTO.getId().equals("userId") &&
                        userDTO.getName().equals("Test User") &&
                        userDTO.getDocumentId().equals("1234567890")
        ));
    }

    @Test
    void shouldProcessTransactionCreatedEvent() {
        TransactionCreated event = new TransactionCreated(
                "transactionId",
                new BigDecimal("200.00"),
                new BigDecimal("5.00"),
                new BigDecimal("195.00"),
                TransactionType.ATM_WITHDRAWAL,
                LocalDateTime.now(),
                "accountId"
        );

        listener.receiveTransactionCreated(event);

        verify(transactionSavedViewUseCase).accept(argThat(transactionDTO ->
                transactionDTO.getId().equals("transactionId") &&
                        transactionDTO.getAmount().compareTo(new BigDecimal("200.00")) == 0 &&
                        transactionDTO.getFee().compareTo(new BigDecimal("5.00")) == 0 &&
                        transactionDTO.getFinalAmount().compareTo(new BigDecimal("195.00")) == 0 &&
                        transactionDTO.getType().equals(TransactionType.ATM_WITHDRAWAL) &&
                        transactionDTO.getAccountId().equals("accountId")
        ));
    }

    @Test
    void shouldProcessAccountUpdatedEvent() {
        AccountBalanceUpdated event = new AccountBalanceUpdated(
                "accountId",
                "123456789",
                new BigDecimal("1500.00"),
                "userId"
        );

        listener.receiveAccountUpdated(event);

        verify(accountSavedViewUseCase).accept(argThat(accountDTO ->
                accountDTO.getId().equals("accountId") &&
                        accountDTO.getAccountNumber().equals("123456789") &&
                        accountDTO.getBalance().compareTo(new BigDecimal("1500.00")) == 0 &&
                        accountDTO.getUserId().equals("userId")
        ));
    }
}
