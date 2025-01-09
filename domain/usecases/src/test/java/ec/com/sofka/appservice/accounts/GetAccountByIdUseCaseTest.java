package ec.com.sofka.appservice.accounts;

import ec.com.sofka.account.Account;
import ec.com.sofka.appservice.gateway.IAccountRepository;
import ec.com.sofka.appservice.queries.usecases.GetAccountByIdUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.when;
public class GetAccountByIdUseCaseTest {

    @Mock
    private IAccountRepository repository;

    private GetAccountByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new GetAccountByIdUseCase(repository);
    }

    @Test
    void getAccountByIdSuccessfully() {
        // Arrange
        String id = "675dbabe03edcf54111957fe";
        Account expectedAccount = new Account(
                id,
                BigDecimal.valueOf(1000),
                "1234567890",
                "Juan Perez"
        );
        expectedAccount.setAccountNumber("123456");

        when(repository.findById(id))
                .thenReturn(Mono.just(expectedAccount));

        // Act & Assert
        StepVerifier.create(useCase.apply(id))
                .expectNext(expectedAccount)
                .verifyComplete();
    }

    @Test
    void getAccountByIdNotFound() {
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
    void getAccountByIdWithInvalidId() {
        // Arrange
        String invalidId = "";

        when(repository.findById(invalidId))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(useCase.apply(invalidId))
                .expectErrorMatches(throwable ->
                        throwable instanceof NoSuchElementException &&
                                throwable.getMessage().equals("No account found with id: " + invalidId)
                )
                .verify();
    }

}
