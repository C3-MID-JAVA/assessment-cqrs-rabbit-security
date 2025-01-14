package ec.com.sofka;

import ec.com.sofka.aggregate.events.AccountCreated;
import ec.com.sofka.aggregate.events.AccountUpdated;
import ec.com.sofka.aggregate.events.TransactionCreated;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.gateway.dto.TransactionDTO;
import ec.com.sofka.queries.usecases.AccountSavedViewUseCase;
import ec.com.sofka.queries.usecases.TransactionSavedViewUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class BusListenerTest {

    @Mock
    private AccountSavedViewUseCase accountSavedViewUseCase;

    @Mock
    private TransactionSavedViewUseCase transactionSavedViewUseCase;

    @InjectMocks
    private BusListener busListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("When receive account created event should process successfully")
    void accountCreatedEventProcessSuccessfully() {
        AccountCreated event = new AccountCreated("accountId", "accountNumber", new BigDecimal("100.0"), "name", "status", "userId");

        doNothing().when(accountSavedViewUseCase).accept(argThat(accountDTO ->
                accountDTO.getId().equals("accountId") &&
                        accountDTO.getName().equals("name") &&
                        accountDTO.getAccountNumber().equals("accountNumber") &&
                        accountDTO.getBalance().equals(new BigDecimal("100.0")) &&
                        accountDTO.getStatus().equals("status") &&
                        accountDTO.getIdUser().equals("userId")
        ));

        busListener.receiveAccountCreated(event);

        verify(accountSavedViewUseCase, times(1)).accept(argThat(accountDTO ->
                accountDTO.getId().equals("accountId") &&
                        accountDTO.getName().equals("name") &&
                        accountDTO.getAccountNumber().equals("accountNumber") &&
                        accountDTO.getBalance().equals(new BigDecimal("100.0")) &&
                        accountDTO.getStatus().equals("status") &&
                        accountDTO.getIdUser().equals("userId")
        ));
    }

    @Test
    @DisplayName("When receive account updated event should process successfully")
    void accountUpdatedEventProcessSuccessfully() {
        AccountUpdated event = new AccountUpdated("accountId", new BigDecimal("200.0"), "accountNumber", "name", "status", "userId");

        doNothing().when(accountSavedViewUseCase).accept(argThat(accountDTO ->
                accountDTO.getId().equals("accountId") &&
                        accountDTO.getName().equals("name") &&
                        accountDTO.getAccountNumber().equals("accountNumber") &&
                        accountDTO.getBalance().equals(new BigDecimal("200.0")) &&
                        accountDTO.getStatus().equals("status") &&
                        accountDTO.getIdUser().equals("userId")
        ));

        busListener.receiveAccountUpdated(event);

        verify(accountSavedViewUseCase, times(1)).accept(argThat(accountDTO ->
                accountDTO.getId().equals("accountId") &&
                        accountDTO.getName().equals("name") &&
                        accountDTO.getAccountNumber().equals("accountNumber") &&
                        accountDTO.getBalance().equals(new BigDecimal("200.0")) &&
                        accountDTO.getStatus().equals("status") &&
                        accountDTO.getIdUser().equals("userId")
        ));
    }

    @Test
    @DisplayName("When receive transaction created event should process successfully")
    void transactionCreatedEventProcessSuccessfully() {
        TransactionCreated event = new TransactionCreated("transactionId", new BigDecimal("50.0"), "type", new BigDecimal("5.0"), "idAccount", "status");

        doNothing().when(transactionSavedViewUseCase).accept(argThat(transactionDTO ->
                transactionDTO.getId().equals("transactionId") &&
                        transactionDTO.getAmount().equals(new BigDecimal("50.0")) &&
                        transactionDTO.getType().equals("type") &&
                        transactionDTO.getCost().equals(new BigDecimal("5.0")) &&
                        transactionDTO.getIdAccount().equals("idAccount") &&
                        transactionDTO.getStatus().equals("status")
        ));

        busListener.receiveTransactionCreated(event);

        verify(transactionSavedViewUseCase, times(1)).accept(argThat(transactionDTO ->
                transactionDTO.getId().equals("transactionId") &&
                        transactionDTO.getAmount().equals(new BigDecimal("50.0")) &&
                        transactionDTO.getType().equals("type") &&
                        transactionDTO.getCost().equals(new BigDecimal("5.0")) &&
                        transactionDTO.getIdAccount().equals("idAccount") &&
                        transactionDTO.getStatus().equals("status")
        ));
    }
}