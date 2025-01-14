package ec.com.sofka.appservice.accounts;

import ec.com.sofka.ConflictException;
import ec.com.sofka.account.Account;
import ec.com.sofka.appservice.gateway.IAccountRepository;
import ec.com.sofka.appservice.gateway.dto.AccountDTO;
import ec.com.sofka.appservice.queries.query.GetByQuery;
import ec.com.sofka.appservice.queries.responses.AccountResponse;
import ec.com.sofka.appservice.queries.usecases.GetAccountByAccountNumberUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GetAccountByAccountNumberUseCaseTest {

    @Mock
    private IAccountRepository accountRepository;

    private GetAccountByAccountNumberUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new GetAccountByAccountNumberUseCase(accountRepository);
    }

    @Test
    void shouldReturnAccountSuccessfullyWhenAccountExists() {
        // Datos de prueba
        String accountNumber = "0123456789";
        String accountId = "account123";
        String customerName = "John Doe";
        BigDecimal balance = BigDecimal.ZERO;
        String status = "ACCOUNT_ACTIVE";

        // Creamos el DTO de la cuenta que será retornado por el repositorio
        AccountDTO accountDTO = new AccountDTO(accountId, customerName, accountNumber, balance, status);

        // Simulamos que el repositorio encuentra la cuenta
        when(accountRepository.findByAccountNumber(accountNumber))
                .thenReturn(Mono.just(accountDTO));

        // Creamos el comando de consulta
        GetByQuery query = new GetByQuery(accountNumber);

        // Ejecutamos el caso de uso
        StepVerifier.create(useCase.get(query))
                .consumeNextWith(response -> {
                    AccountResponse accountResponse = response.getSingleResult().get();

                    // Verificamos que la respuesta contiene los datos correctos
                    assertEquals(accountId, accountResponse.getAccountId());
                    assertEquals(accountNumber, accountResponse.getAccountNumber());
                    assertEquals(customerName,accountResponse.getName());
                    assertEquals(balance, accountResponse.getBalance());
                    assertEquals(status, accountResponse.getStatus());
                })
                .verifyComplete();

        // Verificamos que el repositorio haya sido llamado con el número de cuenta
        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
    }


    @Test
    void shouldThrowConflictExceptionWhenAccountDoesNotExist() {
        // Datos de prueba
        String accountNumber = "0123456789";

        // Simulamos que no se encuentra la cuenta en el repositorio
        when(accountRepository.findByAccountNumber(accountNumber))
                .thenReturn(Mono.empty());

        // Creamos el comando de consulta
        GetByQuery query = new GetByQuery(accountNumber);

        // Ejecutamos el caso de uso y verificamos que se lance la excepción
        StepVerifier.create(useCase.get(query))
                .expectErrorMatches(ex -> ex instanceof ConflictException && ex.getMessage().equals("Account not found by number account."))
                .verify();

        // Verificamos que el repositorio haya sido llamado con el número de cuenta
        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
    }


}
