package ec.com.sofka.appservice.transactions.transactionprocess;

import ec.com.sofka.account.Account;
import ec.com.sofka.appservice.gateway.IAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

class SaveAccountUseCaseTest {

    private IAccountRepository repository;
    private SaveAccountUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(IAccountRepository.class);
        useCase = new SaveAccountUseCase(repository);
    }

    @Test
    void testSaveAccountSuccessfully() {
        // Arrange
        Account account = new Account(BigDecimal.valueOf(1000.00),"123", "John Doe");
        when(repository.save(account)).thenReturn(Mono.just(account));

        // Act & Assert
        StepVerifier.create(useCase.apply(account))
                .expectNext(account)
                .verifyComplete();

        verify(repository).save(account);
    }

    @Test
    void testSaveAccountWithRepositoryError() {
        // Arrange
        Account account = new Account(BigDecimal.valueOf(1000.00),"123", "John Doe");
        String errorMessage = "Database error";

        when(repository.save(account)).thenReturn(Mono.error(new RuntimeException(errorMessage)));

        // Act & Assert
        StepVerifier.create(useCase.apply(account))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals(errorMessage))
                .verify();

        verify(repository).save(account);
    }
}
