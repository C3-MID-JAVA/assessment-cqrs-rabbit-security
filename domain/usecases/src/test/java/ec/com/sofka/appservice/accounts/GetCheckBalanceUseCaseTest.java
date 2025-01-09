package ec.com.sofka.appservice.accounts;

import ec.com.sofka.account.Account;
import ec.com.sofka.appservice.gateway.IAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.when;
public class GetCheckBalanceUseCaseTest {
    @Mock
    private IAccountRepository repository;

    private GetCheckBalanceUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new GetCheckBalanceUseCase(repository);
    }

    @Test
    void getBalanceSuccessfully() {
        // Arrange
        String accountId = "675dbabe03edcf54111957fe";
        BigDecimal expectedBalance = BigDecimal.valueOf(1000.00);

        Account account = new Account(
                accountId,
                expectedBalance,
                "1234567890",
                "Juan Perez"
        );

        when(repository.findById(accountId))
                .thenReturn(Mono.just(account));

        // Act & Assert
        StepVerifier.create(useCase.apply(accountId))
                .expectNext(expectedBalance)
                .verifyComplete();
    }

    @Test
    void getBalanceWithZeroBalance() {
        // Arrange
        String accountId = "675dbabe03edcf54111957fe";
        BigDecimal expectedBalance = BigDecimal.ZERO;

        Account account = new Account(
                accountId,
                expectedBalance,
                "1234567890",
                "Juan Perez"
        );

        when(repository.findById(accountId))
                .thenReturn(Mono.just(account));

        // Act & Assert
        StepVerifier.create(useCase.apply(accountId))
                .expectNext(expectedBalance)
                .verifyComplete();
    }

    @Test
    void getBalanceAccountNotFound() {
        // Arrange
        String nonExistentId = "nonexistent123";

        when(repository.findById(nonExistentId))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(useCase.apply(nonExistentId))
                .expectErrorMatches(throwable ->
                        throwable instanceof NoSuchElementException &&
                                throwable.getMessage().equals("No account found with id: " + nonExistentId)
                )
                .verify();
    }

    @Test
    void getBalanceWithNegativeBalance() {
        // Arrange
        String accountId = "675dbabe03edcf54111957fe";
        BigDecimal expectedBalance = BigDecimal.valueOf(-500.00);

        Account account = new Account(
                accountId,
                expectedBalance,
                "1234567890",
                "Juan Perez"
        );

        when(repository.findById(accountId))
                .thenReturn(Mono.just(account));

        // Act & Assert
        StepVerifier.create(useCase.apply(accountId))
                .expectNext(expectedBalance)
                .verifyComplete();
    }
}
