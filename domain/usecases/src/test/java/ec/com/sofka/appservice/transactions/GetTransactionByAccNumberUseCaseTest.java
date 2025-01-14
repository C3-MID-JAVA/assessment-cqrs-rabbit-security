package ec.com.sofka.appservice.transactions;

import ec.com.sofka.appservice.gateway.IAccountRepository;
import ec.com.sofka.appservice.gateway.ITransactionRepository;
import ec.com.sofka.appservice.queries.query.GetByElementQuery;
import ec.com.sofka.appservice.queries.responses.TransactionResponse;
import ec.com.sofka.appservice.queries.usecases.GetTransactionByAccNumberUseCase;
import ec.com.sofka.appservice.gateway.dto.AccountDTO;
import ec.com.sofka.appservice.gateway.dto.TransactionDTO;
import ec.com.sofka.enums.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class GetTransactionByAccNumberUseCaseTest {

    @Mock
    private IAccountRepository accountRepository;

    @Mock
    private ITransactionRepository transactionRepository;

    private GetTransactionByAccNumberUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new GetTransactionByAccNumberUseCase(transactionRepository, null, accountRepository);
    }

    @Test
    void shouldReturnTransactionsSuccessfullyWhenAccountHasTransactions() {
        // Datos de prueba
        String accountNumber = "0123456789";
        String accountId = "account123";
        String transactionId = "txn001";
        BigDecimal transactionCost = BigDecimal.valueOf(1.50);
        BigDecimal amount = BigDecimal.valueOf(100);
        LocalDateTime transactionDate = LocalDateTime.now();
        TransactionType transactionType = TransactionType.PHYSICAL_PURCHASE;

        // Creamos el DTO de la cuenta que será retornado por el repositorio
        AccountDTO accountDTO = new AccountDTO(accountId, "John Doe", accountNumber, BigDecimal.ZERO, "ACCOUNT_ACTIVE");

        // Creamos el DTO de la transacción que será retornado por el repositorio
        TransactionDTO transactionDTO = new TransactionDTO(transactionId, amount, transactionCost, transactionDate, transactionType, accountId);

        // Simulamos que el repositorio encuentra la cuenta y las transacciones
        when(accountRepository.findByAccountNumber(accountNumber))
                .thenReturn(Mono.just(accountDTO));
        when(transactionRepository.findAllTransactionById(accountId))
                .thenReturn(Flux.just(transactionDTO));

        // Ejecutamos el caso de uso
        StepVerifier.create(useCase.get(new GetByElementQuery(accountNumber)))
                .consumeNextWith(response -> {
                    // Verificamos que la respuesta contiene las transacciones correctas
                    assertEquals(1, response.getMultipleResults().size());
                    TransactionResponse transactionResponse = response.getMultipleResults().get(0);
                    assertEquals(transactionId, transactionResponse.getTransactionId());
                    assertEquals(accountId, transactionResponse.getAccountId());
                    assertEquals(transactionCost, transactionResponse.getTransactionCost());
                    assertEquals(amount, transactionResponse.getAmount());
                    assertEquals(transactionDate, transactionResponse.getTransactionDate());
                    assertEquals(transactionType, transactionResponse.getTransactionType());
                })
                .verifyComplete();

        // Verificamos que los repositorios hayan sido llamados correctamente
        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
        verify(transactionRepository, times(1)).findAllTransactionById(accountId);
    }

    @Test
    void shouldReturnEmptyTransactionsWhenAccountHasNoTransactions() {
        // Datos de prueba
        String accountNumber = "0123456789";
        String accountId = "account123";

        // Creamos el DTO de la cuenta que será retornado por el repositorio
        AccountDTO accountDTO = new AccountDTO(accountId, "John Doe", accountNumber, BigDecimal.ZERO, "ACCOUNT_ACTIVE");

        // Simulamos que el repositorio encuentra la cuenta, pero no hay transacciones para esa cuenta
        when(accountRepository.findByAccountNumber(accountNumber))
                .thenReturn(Mono.just(accountDTO));
        when(transactionRepository.findAllTransactionById(accountId))
                .thenReturn(Flux.empty());  // No hay transacciones para la cuenta

        // Ejecutamos el caso de uso
        StepVerifier.create(useCase.get(new GetByElementQuery(accountNumber)))
                .consumeNextWith(response -> {
                    // Verificamos que la respuesta tiene una lista vacía de transacciones
                    assertTrue(response.getMultipleResults().isEmpty(), "Expected no transactions");
                })
                .verifyComplete();

        // Verificamos que los repositorios hayan sido llamados correctamente
        verify(accountRepository, times(1)).findByAccountNumber(accountNumber);
        verify(transactionRepository, times(1)).findAllTransactionById(accountId);
    }

}
