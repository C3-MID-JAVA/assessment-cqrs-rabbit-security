package ec.com.sofka.appservice.transactions;

import ec.com.sofka.ConflictException;
import ec.com.sofka.appservice.commands.usecases.CreateDepositUseCase;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CreateDepositUseCaseTest {

    @Mock
    private ProcessTransactionUseCase transactionUseCase;
    @InjectMocks
    private CreateDepositUseCase createDepositUseCase;
    @BeforeEach public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test public void testApply() {
        Transaction transaction = new Transaction("675dbabe03edcf54111957fe", BigDecimal.valueOf(2000), BigDecimal.valueOf(1.50),
                LocalDateTime.now(),TransactionType.OTHER_ACCOUNT_DEPOSIT,"123456");

        Transaction processedTransaction = new Transaction("1234567890", BigDecimal.valueOf(2000), BigDecimal.valueOf(1.50),
                LocalDateTime.now(),TransactionType.OTHER_ACCOUNT_DEPOSIT,"123456");

        when(transactionUseCase.apply(any(Transaction.class), any(OperationType.class))) .thenReturn(Mono.just(processedTransaction));
        Mono<Transaction> result = createDepositUseCase.apply(transaction);
        StepVerifier.create(result) .expectNext(processedTransaction) .verifyComplete();
    }

    @Test
    void createDepositSuccessfully() {
        // Arrange
        Transaction transaction = new Transaction("675dbabe03edcf54111957fe", BigDecimal.valueOf(2000), BigDecimal.valueOf(1.50),
                LocalDateTime.now(),TransactionType.OTHER_ACCOUNT_DEPOSIT,"123456");

        when(transactionUseCase.apply(any(Transaction.class), any(OperationType.class)))
                .thenReturn(Mono.just(transaction));

        // Act & Assert
        StepVerifier.create(createDepositUseCase.apply(transaction))
                .expectNext(transaction)
                .verifyComplete();
    }

    @Test
    void createDepositShouldFailWithError() {
        // Arrange
        Transaction transaction = new Transaction("675dbabe03edcf54111957fe", BigDecimal.valueOf(2000), BigDecimal.valueOf(1.50),
                LocalDateTime.now(), TransactionType.OTHER_ACCOUNT_DEPOSIT, "123456");

        when(transactionUseCase.apply(any(Transaction.class), any(OperationType.class)))
                .thenReturn(Mono.error(new ConflictException("Insufficient balance for transaction.")));

        // Act & Assert
        StepVerifier.create(createDepositUseCase.apply(transaction))
                .expectError(ConflictException.class)  // Cambiar a la excepción lanzada realmente
                .verify();
    }

}
