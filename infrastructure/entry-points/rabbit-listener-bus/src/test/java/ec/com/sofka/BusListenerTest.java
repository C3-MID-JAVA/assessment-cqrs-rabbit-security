package ec.com.sofka;
import ec.com.sofka.aggregate.events.AccountCreated;
import ec.com.sofka.aggregate.events.AccountUpdated;
import ec.com.sofka.aggregate.events.TransactionCreated;
import ec.com.sofka.appservice.gateway.dto.AccountDTO;
import ec.com.sofka.appservice.gateway.dto.TransactionDTO;
import ec.com.sofka.appservice.queries.usecases.AccountSavedViewUseCase;
import ec.com.sofka.appservice.queries.usecases.AccountUpdatedViewUseCase;
import ec.com.sofka.appservice.queries.usecases.TransactionSavedViewUseCase;
import ec.com.sofka.enums.TransactionType;
import ec.com.sofka.utils.AccountCreatedProperties;
import ec.com.sofka.utils.AccountUpdatedProperties;
import ec.com.sofka.utils.TransactionCreatedProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BusListenerTest {

    @InjectMocks
    private BusListener busListener;

    @Mock
    private AccountSavedViewUseCase accountSavedViewUseCase;

    @Mock
    private AccountUpdatedViewUseCase accountUpdatedViewUseCase;

    @Mock
    private TransactionSavedViewUseCase transactionSavedViewUseCase;

    @Mock
    private AccountCreatedProperties accountCreatedProperties;

    @Mock
    private AccountUpdatedProperties accountUpdatedProperties;

    @Mock
    private TransactionCreatedProperties transactionCreatedProperties;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReceiveAccountCreated() {
        // Given
        AccountCreated accountCreatedEvent = new AccountCreated("123", "ACC001", BigDecimal.valueOf(1000), "John Doe", "ACTIVE");

        // When
        busListener.receiveAccountCreated(accountCreatedEvent);

        // Then
        verify(accountSavedViewUseCase, times(1)).save(any(AccountDTO.class));
    }

    @Test
    void testReceiveAccountUpdated() {
        // Given
        AccountUpdated accountUpdatedEvent = new AccountUpdated("123", BigDecimal.valueOf(1500), "ACC001", "John Doe", "ACTIVE");

        // When
        busListener.receiveAccountUpdated(accountUpdatedEvent);

        // Then
        verify(accountUpdatedViewUseCase, times(1)).update(any(AccountDTO.class));
    }

    @Test
    void testReceiveTransactionCreated() {
        // Given
        TransactionCreated transactionCreatedEvent = new TransactionCreated("txn123", BigDecimal.valueOf(200), BigDecimal.valueOf(5), LocalDateTime.now(), TransactionType.OTHER_ACCOUNT_DEPOSIT, "123");

        // When
        busListener.receiveTransactionCreated(transactionCreatedEvent);

        // Then
        verify(transactionSavedViewUseCase, times(1)).save(any(TransactionDTO.class));
    }
    @Test
    void testReceiveAccountCreated_shouldHandleException() {
        // Given
        AccountCreated accountCreatedEvent = new AccountCreated("123", "ACC001", BigDecimal.valueOf(1000), "John Doe", "ACTIVE");

        // Simulamos que el método `save` lance una excepción
        doThrow(new RuntimeException("Database error")).when(accountSavedViewUseCase).save(any(AccountDTO.class));

        // When
        try {
            busListener.receiveAccountCreated(accountCreatedEvent);
        } catch (Exception e) {
            // Then
            verify(accountSavedViewUseCase, times(1)).save(any(AccountDTO.class));
            assert(e instanceof RuntimeException);
            assert(e.getMessage().equals("Database error"));
        }
    }

    @Test
    void testReceiveAccountUpdated_shouldHandleException() {
        // Given
        AccountUpdated accountUpdatedEvent = new AccountUpdated("123", BigDecimal.valueOf(1500), "ACC001", "John Doe", "ACTIVE");

        // Simulamos que el método `update` lance una excepción
        doThrow(new RuntimeException("Database error")).when(accountUpdatedViewUseCase).update(any(AccountDTO.class));

        // When
        try {
            busListener.receiveAccountUpdated(accountUpdatedEvent);
        } catch (Exception e) {
            // Then
            verify(accountUpdatedViewUseCase, times(1)).update(any(AccountDTO.class));
            assert(e instanceof RuntimeException);
            assert(e.getMessage().equals("Database error"));
        }
    }

    @Test
    void testReceiveTransactionCreated_shouldHandleException() {
        // Given
        TransactionCreated transactionCreatedEvent = new TransactionCreated("txn123", BigDecimal.valueOf(200), BigDecimal.valueOf(5), LocalDateTime.now(), null, "123");

        // Simulamos que el método `save` lance una excepción
        doThrow(new RuntimeException("Transaction error")).when(transactionSavedViewUseCase).save(any(TransactionDTO.class));

        // When
        try {
            busListener.receiveTransactionCreated(transactionCreatedEvent);
        } catch (Exception e) {
            // Then
            verify(transactionSavedViewUseCase, times(1)).save(any(TransactionDTO.class));
            assert(e instanceof RuntimeException);
            assert(e.getMessage().equals("Transaction error"));
        }
    }

    @Test
    void testReceiveAccountCreated_shouldHandleInvalidData() {
        // Given
        AccountCreated accountCreatedEvent = new AccountCreated("", "", BigDecimal.ZERO, "", "");

        // When
        try {
            busListener.receiveAccountCreated(accountCreatedEvent);
        } catch (Exception e) {
            // Then
            assert(e instanceof IllegalArgumentException);
            assert(e.getMessage().equals("Invalid account data"));
        }
    }

    @Test
    void testReceiveAccountUpdated_shouldHandleInvalidData() {
        // Given
        AccountUpdated accountUpdatedEvent = new AccountUpdated("", BigDecimal.ZERO, "", "", "");

        // When
        try {
            busListener.receiveAccountUpdated(accountUpdatedEvent);
        } catch (Exception e) {
            // Then
            assert(e instanceof IllegalArgumentException);
            assert(e.getMessage().equals("Invalid account update data"));
        }
    }

    @Test
    void testReceiveTransactionCreated_shouldHandleInvalidData() {
        // Given
        TransactionCreated transactionCreatedEvent = new TransactionCreated("txn123", null, null, LocalDateTime.now(), null, "123");

        // When
        try {
            busListener.receiveTransactionCreated(transactionCreatedEvent);
        } catch (Exception e) {
            // Then
            assert(e instanceof IllegalArgumentException);
            assert(e.getMessage().equals("Invalid transaction data"));
        }
    }
}
