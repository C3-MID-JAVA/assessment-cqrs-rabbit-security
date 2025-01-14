package ec.com.sofka.appservice.accounts;

import ec.com.sofka.account.Account;
import ec.com.sofka.ConflictException;
import ec.com.sofka.account.values.AccountId;
import ec.com.sofka.account.values.objects.Balance;
import ec.com.sofka.account.values.objects.NumberAcc;
import ec.com.sofka.account.values.objects.Owner;
import ec.com.sofka.account.values.objects.Status;
import ec.com.sofka.aggregate.events.AccountCreated;
import ec.com.sofka.appservice.commands.CreateAccountCommand;
import ec.com.sofka.appservice.commands.usecases.CreateAccountUseCase;
import ec.com.sofka.appservice.gateway.IAccountRepository;
import ec.com.sofka.appservice.gateway.IBusEvent;
import ec.com.sofka.appservice.gateway.IEventStore;
import ec.com.sofka.appservice.gateway.dto.AccountDTO;
import ec.com.sofka.generics.domain.DomainEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreateAccountUseCaseTest {

    @Mock
    private IAccountRepository accountRepository;

    @Mock
    private IEventStore eventStore;  // Simulamos el repositorio de eventos

    @Mock
    private IBusEvent busEvent;  // Simulamos el bus de eventos

    // Inyectamos las dependencias mockeadas en el caso de uso
    @InjectMocks
    private CreateAccountUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateAccountSuccessfully() {
        // Datos de prueba
        String accountNumber = "0123456789";
        String customerName = "John Doe";
        BigDecimal balance = BigDecimal.ZERO;

        // Creamos los objetos de valor para la cuenta
        AccountId accountId = AccountId.of("account123");
        Balance accountBalance = Balance.of(balance);
        NumberAcc accountNumberObj = NumberAcc.of(accountNumber);
        Owner accountOwner = Owner.of(customerName);
        Status accountStatus = Status.of("ACTIVE");

        // Creamos la cuenta con los valores esperados
        Account account = new Account(accountId, accountBalance, accountNumberObj, accountOwner, accountStatus);

        // Creamos un AccountDTO que será guardado por el repositorio
        AccountDTO accountDTO = new AccountDTO(
                account.getId().getValue(),
                account.getOwner().getValue(),
                account.getAccountNumber().getValue(),
                account.getBalance().getValue(),
                account.getStatus().getValue()
        );

        // Simulamos que no existe ninguna cuenta con el mismo número
        when(accountRepository.findByAccountNumber(accountNumber))
                .thenReturn(Mono.empty());  // No se encuentra ninguna cuenta existente

        // Creamos el comando para la prueba
        CreateAccountCommand command = new CreateAccountCommand(accountNumber, customerName, balance);

        // Simulamos que el repositorio de eventos guarda los eventos correctamente
        when(eventStore.save(any(DomainEvent.class)))
                .thenReturn(Mono.just(new AccountCreated()));  // Simulamos el guardado exitoso del evento

        // Simulamos que el bus de eventos envía el evento correctamente
        doNothing().when(busEvent).sendEventAccountCreated(any(Mono.class));

        // Ejecutamos el caso de uso
        StepVerifier.create(useCase.execute(command))
                .consumeNextWith(response -> {
                    // Verificamos que la respuesta contiene los datos correctos
                    assertEquals(accountNumber, response.getAccountNumber());
                    assertEquals(customerName, response.getName());
                    assertEquals(balance, response.getBalance());
                    assertEquals("ACCOUNT_ACTIVE", response.getStatus());  // El estado se asigna de forma predeterminada en el comando
                })
                .verifyComplete();

        // Verificamos que el repositorio de eventos haya sido llamado para guardar los eventos
        verify(eventStore, times(1)).save(any(DomainEvent.class));

        // Verificamos que el bus de eventos haya sido llamado para enviar el evento
        verify(busEvent, times(1)).sendEventAccountCreated(any(Mono.class));

        // Verificamos que el repositorio de cuentas **no** haya sido llamado
        verify(accountRepository, never()).save(any(AccountDTO.class));
    }

    @Test
    void shouldThrowConflictExceptionWhenAccountNumberAlreadyExists() {
        // Datos de prueba
        String accountNumber = "0123456789";
        String customerName = "John Doe";
        BigDecimal balance = BigDecimal.ZERO;

        // Creamos el comando para la prueba
        CreateAccountCommand command = new CreateAccountCommand(accountNumber, customerName, balance);

        // Simulamos que ya existe una cuenta con el mismo número
        AccountDTO existingAccountDTO = new AccountDTO("account123", customerName, accountNumber, balance, "ACCOUNT_ACTIVE");
        when(accountRepository.findByAccountNumber(accountNumber))
                .thenReturn(Mono.just(existingAccountDTO));  // Simulamos una cuenta existente

        // Ejecutamos el caso de uso y verificamos que se lanza una excepción de tipo ConflictException
        StepVerifier.create(useCase.execute(command))
                .expectErrorMatches(throwable -> throwable instanceof ConflictException &&
                        throwable.getMessage().equals("The account number is already registered."))
                .verify();

        // Verificamos que el repositorio de eventos **no** haya sido llamado
        verify(eventStore, never()).save(any(DomainEvent.class));

        // Verificamos que el bus de eventos **no** haya sido llamado
        verify(busEvent, never()).sendEventAccountCreated(any(Mono.class));

        // Verificamos que el repositorio de cuentas haya sido consultado una vez
        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
    }


}
