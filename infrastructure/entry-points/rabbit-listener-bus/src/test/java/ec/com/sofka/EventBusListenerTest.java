package ec.com.sofka;

import ec.com.sofka.aggregates.account.events.*;
import ec.com.sofka.aggregates.auth.events.UserCreated;
import ec.com.sofka.aggregates.auth.events.UserUpdated;
import ec.com.sofka.queries.usecases.*;
import ec.com.sofka.utils.enums.RoleEnum;
import ec.com.sofka.utils.enums.StatusEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventBusListenerTest {

    @Mock
    private UserSavedViewUseCase userSavedViewUseCase;

    @Mock
    private CustomerSavedViewUseCase customerSavedViewUseCase;

    @Mock
    private AccountSavedViewUseCase accountSavedViewUseCase;

    @Mock
    private TransactionTypeSavedViewUseCase transactionTypeSavedViewUseCase;

    @Mock
    private TransactionSavedViewUseCase transactionSavedViewUseCase;

    @InjectMocks
    private EventBusListener eventBusListener;


    @Test
    @DisplayName("When receive user created event should process successfully")
    void whenReceiveUserCreatedEvent_shouldProcessSuccessfully() {
        UserCreated userCreated = new UserCreated("123", "Jhon", "Doe", "test@test.com", "123", RoleEnum.ROLE_EXECUTIVE);

        doNothing().when(userSavedViewUseCase).accept(argThat(userDTO ->
                userDTO.getId().equals("123") &&
                        userDTO.getFirstName().equals("Jhon") &&
                        userDTO.getEmail().equals("test@test.com")
        ));

        eventBusListener.receiveUserCreate(userCreated);

        verify(userSavedViewUseCase, times(1)).accept(argThat(userDTO ->
                userDTO.getId().equals("123") &&
                        userDTO.getFirstName().equals("Jhon") &&
                        userDTO.getEmail().equals("test@test.com")
        ));
    }

    @Test
    @DisplayName("When receive user updated event should process and update user view")
    void whenReceiveUserUpdatedEvent_shouldProcessAndUpdateUserView() {
        UserUpdated userUpdated = new UserUpdated("123", "Jhon", "Doe", "test@test.com", "123", RoleEnum.ROLE_EXECUTIVE);

        doNothing().when(userSavedViewUseCase).accept(argThat(userDTO ->
                userDTO.getId().equals("123") &&
                        userDTO.getFirstName().equals("Jhon") &&
                        userDTO.getEmail().equals("test@test.com")
        ));

        eventBusListener.receiveUserUpdated(userUpdated);

        verify(userSavedViewUseCase, times(1)).accept(argThat(userDTO ->
                userDTO.getId().equals("123") &&
                        userDTO.getFirstName().equals("Jhon") &&
                        userDTO.getEmail().equals("test@test.com")
        ));
    }

    @Test
    @DisplayName("When receive customer created event should process and save customer view")
    void whenReceiveCustomerCreatedEvent_shouldProcessAndSaveCustomerView() {
        CustomerCreated customerCreated = new CustomerCreated("123", "Jhon", "Doe", "1234567890", StatusEnum.ACTIVE);

        doNothing().when(customerSavedViewUseCase).accept(argThat(customerDTO ->
                customerDTO.getId().equals("123") &&
                        customerDTO.getFirstName().equals("Jhon") &&
                        customerDTO.getIdentityCard().equals("1234567890")
        ));

        eventBusListener.receiveCustomerCreate(customerCreated);

        verify(customerSavedViewUseCase, times(1)).accept(argThat(customerDTO ->
                customerDTO.getId().equals("123") &&
                        customerDTO.getFirstName().equals("Jhon") &&
                        customerDTO.getIdentityCard().equals("1234567890")
        ));

    }

    @Test
    @DisplayName("When receive account created event should process and save account view")
    void whenReceiveAccountCreatedEvent_shouldProcessAndSaveAccountView() {
        AccountCreated accountCreated = new AccountCreated("123", "1234567890", new BigDecimal(100), StatusEnum.ACTIVE, null);

        doNothing().when(accountSavedViewUseCase).accept(argThat(accountDTO ->
                accountDTO.getId().equals("123") &&
                        accountDTO.getAccountNumber().equals("1234567890") &&
                        accountDTO.getStatus().name().equals(StatusEnum.ACTIVE.name())
        ));

        eventBusListener.receiveAccountCreate(accountCreated);

        verify(accountSavedViewUseCase, times(1)).accept(argThat(accountDTO ->
                accountDTO.getId().equals("123") &&
                        accountDTO.getAccountNumber().equals("1234567890") &&
                        accountDTO.getStatus().name().equals(StatusEnum.ACTIVE.name())
        ));
    }

    @Test
    @DisplayName("When receive account updated event should process and update account view")
    void whenReceiveAccountUpdatedEvent_shouldProcessAndUpdateAccountView() {
        AccountUpdated accountUpdated = new AccountUpdated("123", "1234567890", new BigDecimal(100), StatusEnum.ACTIVE, null);

        doNothing().when(accountSavedViewUseCase).accept(argThat(accountDTO ->
                accountDTO.getId().equals("123") &&
                        accountDTO.getAccountNumber().equals("1234567890") &&
                        accountDTO.getStatus().name().equals(StatusEnum.ACTIVE.name())
        ));

        eventBusListener.receiveAccountUpdate(accountUpdated);

        verify(accountSavedViewUseCase, times(1)).accept(argThat(accountDTO ->
                accountDTO.getId().equals("123") &&
                        accountDTO.getAccountNumber().equals("1234567890") &&
                        accountDTO.getStatus().name().equals(StatusEnum.ACTIVE.name())
        ));

    }

    @Test
    @DisplayName("When receive transaction type created event should process and save transaction type view")
    void whenReceiveTransactionTypeCreatedEvent_shouldProcessAndSaveTransactionTypeView() {
        TransactionTypeCreated transactionTypeCreated = new TransactionTypeCreated("123", "Retire", "retire money", new BigDecimal(10), true, true, StatusEnum.ACTIVE);

        doNothing().when(transactionTypeSavedViewUseCase).accept(argThat(transactionTypeDTO ->
                transactionTypeDTO.getId().equals("123") &&
                        transactionTypeDTO.getType().equals("Retire") &&
                        transactionTypeDTO.getStatus().name().equals(StatusEnum.ACTIVE.name())
        ));

        eventBusListener.receiveTransactionTypeCreate(transactionTypeCreated);

        verify(transactionTypeSavedViewUseCase, times(1)).accept(argThat(transactionTypeDTO ->
                transactionTypeDTO.getId().equals("123") &&
                        transactionTypeDTO.getType().equals("Retire") &&
                        transactionTypeDTO.getStatus().name().equals(StatusEnum.ACTIVE.name())
        ));

    }

    @Test
    @DisplayName("When receive transaction created event should process and save transaction view")
    void whenReceiveTransactionCreatedEvent_shouldProcessAndSaveTransactionView() {
        TransactionCreated transactionCreated = new TransactionCreated("123", "1234567890", "retiro", new BigDecimal(100), "13-01-2025 20:40:23", null, null);

        doNothing().when(transactionSavedViewUseCase).accept(argThat(transactionDTO ->
                transactionDTO.getId().equals("123") &&
                        transactionDTO.getAccountNumber().equals("1234567890") &&
                        transactionDTO.getProcessingDate().equals("13-01-2025 20:40:23")
        ));

        eventBusListener.receiveTransactionCreate(transactionCreated);

        verify(transactionSavedViewUseCase, times(1)).accept(argThat(transactionDTO ->
                transactionDTO.getId().equals("123") &&
                        transactionDTO.getAccountNumber().equals("1234567890") &&
                        transactionDTO.getProcessingDate().equals("13-01-2025 20:40:23")
        ));

    }

}