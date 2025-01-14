package ec.com.sofka.appservice.transactions;

import ec.com.sofka.ConflictException;
import ec.com.sofka.appservice.commands.CreateTransactionCommand;
import ec.com.sofka.appservice.commands.usecases.CreateWithDrawalUseCase;
import ec.com.sofka.appservice.queries.responses.TransactionResponse;
import ec.com.sofka.transaction.Transaction;
import ec.com.sofka.appservice.commands.usecases.ProcessTransactionUseCase;
import ec.com.sofka.enums.OperationType;
import ec.com.sofka.enums.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreateWithDrawalUseCaseTest {


    @Mock
    private ProcessTransactionUseCase processTransactionUseCase;

    private CreateWithDrawalUseCase createWithDrawalUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createWithDrawalUseCase = new CreateWithDrawalUseCase(processTransactionUseCase, null);
    }

    @Test
    void shouldProcessWithdrawalSuccessfully() {
        // Datos de prueba para la transacción
        String transactionId = "txn123";
        String accountId = "account123";
        BigDecimal amount = new BigDecimal("50.00");
        BigDecimal transactionCost = new BigDecimal("2.00");
        LocalDateTime transactionDate = LocalDateTime.now();
        TransactionType transactionType = TransactionType.ATM_WITHDRAWAL;
        String customerId = "customer123";

        // Crear el comando de transacción
        CreateTransactionCommand transactionCommand = new CreateTransactionCommand(
                transactionCost,
                amount,
                transactionDate,
                transactionType,
                accountId,
                customerId
        );

        // Crear la respuesta esperada
        TransactionResponse expectedResponse = new TransactionResponse(
                transactionId,
                customerId,
                accountId,
                transactionCost,
                amount,
                transactionDate,
                transactionType
        );

        // Simulamos que el proceso de transacción devuelve una respuesta exitosa
        when(processTransactionUseCase.apply(transactionCommand, OperationType.WITHDRAWAL))
                .thenReturn(Mono.just(expectedResponse));

        // Ejecutamos el caso de uso
        StepVerifier.create(createWithDrawalUseCase.execute(transactionCommand))
                .consumeNextWith(response -> {
                    // Verificamos que la respuesta sea la esperada
                    assertEquals(expectedResponse.getTransactionId(), response.getTransactionId());
                    assertEquals(expectedResponse.getAccountId(), response.getAccountId());
                    assertEquals(expectedResponse.getAmount(), response.getAmount());
                    assertEquals(expectedResponse.getTransactionType(), response.getTransactionType());
                })
                .verifyComplete();

        // Verificamos que ProcessTransactionUseCase haya sido llamado con los parámetros correctos
        verify(processTransactionUseCase, times(1)).apply(transactionCommand, OperationType.WITHDRAWAL);
    }


    @Test
    void shouldHandleErrorIfTransactionFails() {
        // Datos de prueba para la transacción
        String transactionId = "txn123";
        String accountId = "account123";
        BigDecimal amount = new BigDecimal("50.00");
        BigDecimal transactionCost = new BigDecimal("2.00");
        LocalDateTime transactionDate = LocalDateTime.now();
        TransactionType transactionType = TransactionType.ATM_WITHDRAWAL;
        String customerId = "customer123";

        // Crear el comando de transacción
        CreateTransactionCommand transactionCommand = new CreateTransactionCommand(
                transactionCost,
                amount,
                transactionDate,
                transactionType,
                accountId,
                customerId
        );

        // Simulamos que el proceso de transacción lanza un error
        when(processTransactionUseCase.apply(transactionCommand, OperationType.WITHDRAWAL))
                .thenReturn(Mono.error(new RuntimeException("Error al procesar la transacción")));

        // Ejecutamos el caso de uso y verificamos que se maneja el error correctamente
        StepVerifier.create(createWithDrawalUseCase.execute(transactionCommand))
                .expectErrorMatches(ex -> ex instanceof RuntimeException && ex.getMessage().equals("Error al procesar la transacción"))
                .verify();

        // Verificamos que ProcessTransactionUseCase haya sido llamado con los parámetros correctos
        verify(processTransactionUseCase, times(1)).apply(transactionCommand, OperationType.WITHDRAWAL);
    }

}
