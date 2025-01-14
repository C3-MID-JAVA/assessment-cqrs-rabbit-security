package ec.com.sofka.appservice.accounts;

import ec.com.sofka.account.Account;
import ec.com.sofka.appservice.gateway.IAccountRepository;
import ec.com.sofka.appservice.gateway.IEventStore;
import ec.com.sofka.appservice.gateway.dto.AccountDTO;
import ec.com.sofka.appservice.queries.query.GetByElementQuery;
import ec.com.sofka.appservice.queries.responses.AccountResponse;
import ec.com.sofka.appservice.queries.usecases.GetAccountByIdUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GetAccountByIdUseCaseTest {

    @Mock
    private IAccountRepository accountRepository;

    @Mock
    private IEventStore eventRepository;

    private GetAccountByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new GetAccountByIdUseCase(accountRepository, eventRepository);
    }

    @Test
    void shouldReturnAccountSuccessfullyWhenAccountExists() {
        // Datos de prueba
        String accountId = "account123";
        String accountNumber = "0123456789";
        String customerName = "John Doe";
        BigDecimal balance = BigDecimal.ZERO;
        String status = "ACCOUNT_ACTIVE";

        // Creamos el DTO de la cuenta que será retornado por el repositorio
        AccountDTO accountDTO = new AccountDTO(accountId, customerName, accountNumber, balance, status);

        // Simulamos que el repositorio encuentra la cuenta por ID
        when(accountRepository.findById(accountId))
                .thenReturn(Mono.just(accountDTO));

        // Creamos el comando de consulta
        GetByElementQuery query = new GetByElementQuery(accountId);

        // Ejecutamos el caso de uso
        StepVerifier.create(useCase.get(query))
                .consumeNextWith(response -> {
                    AccountResponse accountResponse = response.getSingleResult().get();

                    // Verificamos que la respuesta contiene los datos correctos
                    assertEquals(accountId, accountResponse.getAccountId());
                    assertEquals(accountNumber, accountResponse.getAccountNumber());
                    assertEquals(customerName, accountResponse.getName());
                    assertEquals(balance, accountResponse.getBalance());
                    assertEquals(status, accountResponse.getStatus());
                })
                .verifyComplete();

        // Verificamos que el repositorio haya sido llamado con el ID de la cuenta
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void shouldThrowNoSuchElementExceptionWhenAccountDoesNotExist() {
        // Datos de prueba
        String accountId = "account123";

        // Simulamos que no se encuentra la cuenta en el repositorio
        when(accountRepository.findById(accountId))
                .thenReturn(Mono.empty());

        // Creamos el comando de consulta
        GetByElementQuery query = new GetByElementQuery(accountId);

        // Ejecutamos el caso de uso y verificamos que se lance la excepción
        StepVerifier.create(useCase.get(query))
                .expectErrorMatches(ex -> ex instanceof NoSuchElementException && ex.getMessage().equals("Account not found by id."))
                .verify();

        // Verificamos que el repositorio haya sido llamado con el ID de la cuenta
        verify(accountRepository, times(1)).findById(accountId);
    }

}
