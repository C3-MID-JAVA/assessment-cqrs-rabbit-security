package ec.com.sofka.appservice.accounts;

import ec.com.sofka.account.Account;
import ec.com.sofka.appservice.gateway.IAccountRepository;
import ec.com.sofka.appservice.queries.usecases.GetAccountByAccountNumberUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.when;

public class GetAccountByAccountNumberUseCaseTest {

    @Mock
    private IAccountRepository repository;

    private GetAccountByAccountNumberUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new GetAccountByAccountNumberUseCase(repository);
    }

    @Test
    void getAccountByNumberSuccessfully() {
        // Arrange
        String accountNumber = "123456";
        Account expectedAccount = new Account(
                "675dbabe03edcf54111957fe",
                BigDecimal.valueOf(1000),
                "1234567890",
                "Juan Perez"
        );
        expectedAccount.setAccountNumber(accountNumber);

        when(repository.findByAccountNumber(accountNumber))
                .thenReturn(Mono.just(expectedAccount));

        // Act & Assert
        StepVerifier.create(useCase.apply(accountNumber))
                .expectNext(expectedAccount)
                .verifyComplete();
    }

    @Test
    void getAccountByNumberNotFound() {
        // Arrange
        String accountNumber = "nonexistent123";

        when(repository.findByAccountNumber(accountNumber))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(useCase.apply(accountNumber))
                .expectErrorMatches(throwable ->
                        throwable instanceof NoSuchElementException &&
                                throwable.getMessage().equals("The account with the provided account number does not exist")
                )
                .verify();
    }

}
