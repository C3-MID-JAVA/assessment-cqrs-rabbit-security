package ec.com.sofka.appservice.transactions.transactionprocess;


import ec.com.sofka.transaction.Transaction;
import ec.com.sofka.enums.TransactionType;
import ec.com.sofka.appservice.gateway.ITransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class SaveTransactionUseCaseTest {

    private ITransactionRepository repository;
    private SaveTransactionUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(ITransactionRepository.class);
        useCase = new SaveTransactionUseCase(repository);
    }

    @Test
    void testSaveTransactionSuccessfully() {
        // Arrange
        Transaction transaction = new Transaction(null, BigDecimal.valueOf(200), BigDecimal.ZERO, null, TransactionType.ATM_DEPOSIT, "nonexistent");
        Transaction savedTransaction = new Transaction(null, BigDecimal.valueOf(200), BigDecimal.ZERO, null, TransactionType.ATM_DEPOSIT, "nonexistent");

        when(repository.save(transaction)).thenAnswer(invocation -> {
            Transaction arg = invocation.getArgument(0);
            arg.setDate(LocalDateTime.now());
            return Mono.just(arg);
        });

        // Act & Assert
        StepVerifier.create(useCase.apply(transaction))
                .assertNext(saved -> {
                    assertNotNull(saved.getDate(), "Transaction date should not be null");
                })
                .verifyComplete();

        verify(repository).save(transaction);
    }

    @Test
    void testSaveTransactionWithRepositoryError() {
        // Arrange
        Transaction transaction = new Transaction(null, BigDecimal.valueOf(200), BigDecimal.ZERO, null, TransactionType.ATM_DEPOSIT, "nonexistent");
        String errorMessage = "Database error";

        when(repository.save(transaction)).thenReturn(Mono.error(new RuntimeException(errorMessage)));

        // Act & Assert
        StepVerifier.create(useCase.apply(transaction))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals(errorMessage))
                .verify();

        verify(repository).save(transaction);
    }
}

