package ec.com.sofka.appservice.transactions;

import ec.com.sofka.ConflictException;
import ec.com.sofka.appservice.commands.usecases.CreateWithDrawalUseCase;
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

public class CreateWithDrawalUseCaseTest {


    @Mock
    private ProcessTransactionUseCase transactionUseCase;
    @InjectMocks
    private CreateWithDrawalUseCase createWithDrawalUseCase;
    @BeforeEach public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void createWithDrawalShouldSucceed() {
        // Arrange
        Transaction transaction = new Transaction("123456", BigDecimal.valueOf(500), BigDecimal.valueOf(2),
                LocalDateTime.now(), TransactionType.ATM_WITHDRAWAL, "675dbabe03edcf54111957fe");

        when(transactionUseCase.apply(any(Transaction.class), any(OperationType.class)))
                .thenReturn(Mono.just(transaction));

        // Act & Assert
        StepVerifier.create(createWithDrawalUseCase.apply(transaction))
                .expectNext(transaction)
                .verifyComplete();
    }

    @Test
    void createWithDrawalShouldFailWithInsufficientBalance() {
        // Arrange
        Transaction transaction = new Transaction("123456", BigDecimal.valueOf(2000), BigDecimal.valueOf(1.50),
                LocalDateTime.now(), TransactionType.ATM_WITHDRAWAL, "675dbabe03edcf54111957fe");

        when(transactionUseCase.apply(any(Transaction.class), any(OperationType.class)))
                .thenReturn(Mono.error(new ConflictException("Insufficient balance for transaction.")));

        // Act & Assert
        StepVerifier.create(createWithDrawalUseCase.apply(transaction))
                .expectErrorMatches(throwable -> throwable instanceof ConflictException &&
                        throwable.getMessage().equals("Insufficient balance for transaction."))
                .verify();
    }

}
