package ec.com.sofka.appservice.accounts;

import ec.com.sofka.account.Account;
import ec.com.sofka.appservice.gateway.IAccountRepository;
import ec.com.sofka.appservice.gateway.IBusEvent;
import ec.com.sofka.appservice.gateway.IEventStore;
import ec.com.sofka.appservice.gateway.dto.AccountDTO;
import ec.com.sofka.appservice.queries.responses.AccountResponse;
import ec.com.sofka.appservice.queries.usecases.GetAllAccountsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GetAllAccountsUseCaseTest {

    @Mock
    private IAccountRepository accountRepository;

    @Mock
    private IBusEvent busMessage;

    @Mock
    private IEventStore eventRepository;

    private GetAllAccountsUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new GetAllAccountsUseCase(accountRepository, busMessage, eventRepository);
    }

    @Test
    void shouldReturnAllAccountsSuccessfullyWhenAccountsExist() {
        // Datos de prueba para las cuentas
        AccountDTO accountDTO1 = new AccountDTO("account123", "John Doe", "1234567800", BigDecimal.ZERO, "ACCOUNT_ACTIVE");
        AccountDTO accountDTO2 = new AccountDTO("account124", "Jane Doe", "8765432100", BigDecimal.TEN, "ACCOUNT_ACTIVE");

        // Simulamos que el repositorio encuentra las cuentas
        when(accountRepository.findAll())
                .thenReturn(Flux.just(accountDTO1, accountDTO2));

        // Ejecutamos el caso de uso
        StepVerifier.create(useCase.get())
                .consumeNextWith(response -> {
                    // Verificamos que la respuesta contiene las cuentas correctas
                    assertEquals(2, response.getMultipleResults().size());

                    // Verificamos los datos de la primera cuenta
                    AccountResponse accountResponse1 = response.getMultipleResults().get(0);
                    assertEquals("account123", accountResponse1.getAccountId());
                    assertEquals("1234567800", accountResponse1.getAccountNumber());
                    assertEquals("John Doe", accountResponse1.getName());
                    assertEquals(BigDecimal.ZERO, accountResponse1.getBalance());
                    assertEquals("ACCOUNT_ACTIVE", accountResponse1.getStatus());

                    // Verificamos los datos de la segunda cuenta
                    AccountResponse accountResponse2 = response.getMultipleResults().get(1);
                    assertEquals("account124", accountResponse2.getAccountId());
                    assertEquals("8765432100", accountResponse2.getAccountNumber());
                    assertEquals("Jane Doe", accountResponse2.getName());
                    assertEquals(BigDecimal.TEN, accountResponse2.getBalance());
                    assertEquals("ACCOUNT_ACTIVE", accountResponse2.getStatus());
                })
                .verifyComplete();

        // Verificamos que el repositorio haya sido llamado para obtener todas las cuentas
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoAccountsExist() {
        // Simulamos que no hay cuentas en el repositorio
        when(accountRepository.findAll())
                .thenReturn(Flux.empty());

        // Ejecutamos el caso de uso
        StepVerifier.create(useCase.get())
                .consumeNextWith(response -> {
                    // Verificamos que la respuesta esté vacía
                    assertEquals(0, response.getMultipleResults().size());
                })
                .verifyComplete();

        // Verificamos que el repositorio haya sido llamado para obtener todas las cuentas
        verify(accountRepository, times(1)).findAll();
    }
}
