package ec.com.sofka.appservice.transactions;
import ec.com.sofka.transaction.Transaction;
import ec.com.sofka.enums.TransactionType;
import ec.com.sofka.appservice.gateway.ITransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
public class GetTransactionsUseCaseTest {

    @Mock
    private ITransactionRepository repository;

    private GetTransactionsUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new GetTransactionsUseCase(repository);
    }

    @Test
    void getAllTransactionsSuccessfully() {
        // Arrange
        Transaction transaction1 = new Transaction(
                "123456",
                BigDecimal.valueOf(500),
                BigDecimal.valueOf(2),
                LocalDateTime.now(),
                TransactionType.ATM_WITHDRAWAL,
                "1"
        );
        Transaction transaction2 = new Transaction(
                "789012",
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(5),
                LocalDateTime.now(),
                TransactionType.ATM_WITHDRAWAL,
                "2"
        );

        when(repository.findAll())
                .thenReturn(Flux.just(transaction1, transaction2));

        // Act & Assert
        StepVerifier.create(useCase.apply())
                .expectNext(transaction1)
                .expectNext(transaction2)
                .verifyComplete();
    }

    @Test
    void getAllTransactionsEmpty() {
        // Arrange
        when(repository.findAll())
                .thenReturn(Flux.empty());

        // Act & Assert
        StepVerifier.create(useCase.apply())
                .verifyComplete();
    }
}
