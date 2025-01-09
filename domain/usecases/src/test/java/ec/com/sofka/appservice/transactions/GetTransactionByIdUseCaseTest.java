package ec.com.sofka.appservice.transactions;

import ec.com.sofka.appservice.queries.usecases.GetTransactionByAccNumberUseCase;
import ec.com.sofka.transaction.Transaction;
import ec.com.sofka.enums.TransactionType;
import ec.com.sofka.appservice.gateway.ITransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.when;
public class GetTransactionByIdUseCaseTest {

    @Mock
    private ITransactionRepository repository;

    private GetTransactionByAccNumberUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new GetTransactionByAccNumberUseCase(repository);
    }

    @Test
    void getTransactionByIdSuccessfully() {
        // Arrange
        String id = "675dbabe03edcf54111957fe";
        Transaction expectedTransaction = new Transaction(
                id,BigDecimal.valueOf(500),BigDecimal.valueOf(2),
                LocalDateTime.now(),TransactionType.ATM_WITHDRAWAL,"123456"
        );

        when(repository.findById(id)).thenReturn(Mono.just(expectedTransaction));

        // Act & Assert
        StepVerifier.create(useCase.apply(id))
                .expectNext(expectedTransaction)
                .verifyComplete();
    }

    @Test
    void getTransactionByIdNotFound() {
        // Arrange
        String nonExistentId = "nonexistent123";

        when(repository.findById(nonExistentId))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(useCase.apply(nonExistentId))
                .expectErrorMatches(throwable ->
                        throwable instanceof NoSuchElementException &&
                                throwable.getMessage().equals("No transaction found with id: " + nonExistentId)
                )
                .verify();
    }

    @Test
    void getTransactionByIdWithInvalidId() {
        // Arrange
        String invalidId = "";

        when(repository.findById(invalidId))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(useCase.apply(invalidId))
                .expectErrorMatches(throwable ->
                        throwable instanceof NoSuchElementException &&
                                throwable.getMessage().equals("No transaction found with id: " + invalidId)
                )
                .verify();
    }
}
