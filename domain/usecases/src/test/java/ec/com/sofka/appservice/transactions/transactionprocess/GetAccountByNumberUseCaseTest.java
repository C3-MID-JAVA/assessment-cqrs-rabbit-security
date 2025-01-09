package ec.com.sofka.appservice.transactions.transactionprocess;

import ec.com.sofka.account.Account;
import ec.com.sofka.appservice.queries.usecases.GetAccountByAccountNumberUseCase;
import ec.com.sofka.appservice.gateway.IAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;

public class GetAccountByNumberUseCaseTest {

    private IAccountRepository repository;
    private GetAccountByAccountNumberUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(IAccountRepository.class);
        useCase = new GetAccountByAccountNumberUseCase(repository);
    }

    @Test
    void testApplyAccountFound() {
        // Arrange
        String accountNumber = "123456789";
        Account mockAccount = new Account(BigDecimal.valueOf(1000.00),accountNumber, "John Doe" );

        when(repository.findByAccountNumber(accountNumber)).thenReturn(Mono.just(mockAccount));

        // Act & Assert
        StepVerifier.create(useCase.apply(accountNumber))
                .expectNext(mockAccount)
                .verifyComplete();

        verify(repository).findByAccountNumber(accountNumber);
    }

    @Test
    void testApplyAccountNotFound() {
        // Arrange
        String accountNumber = "987654321";
        when(repository.findByAccountNumber(accountNumber)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(useCase.apply(accountNumber))
                .expectErrorMatches(throwable -> throwable instanceof NoSuchElementException &&
                        throwable.getMessage().equals("The account with the provided account number does not exist"))
                .verify();

        verify(repository).findByAccountNumber(accountNumber);
    }

    @Test
    void testApplyRepositoryError() {
        // Arrange
        String accountNumber = "1122334455";
        when(repository.findByAccountNumber(accountNumber)).thenReturn(Mono.error(new RuntimeException("Database error")));

        // Act & Assert
        StepVerifier.create(useCase.apply(accountNumber))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Database error"))
                .verify();

        verify(repository).findByAccountNumber(accountNumber);
    }
}
