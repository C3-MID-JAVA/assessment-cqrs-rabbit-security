package ec.com.sofka.appservice.accounts;

import ec.com.sofka.account.Account;
import ec.com.sofka.ConflictException;
import ec.com.sofka.appservice.commands.usecases.CreateAccountUseCase;
import ec.com.sofka.appservice.gateway.IAccountRepository;
import ec.com.sofka.appservice.gateway.IBusEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreateAccountUseCaseTest {

    @Mock
    private IAccountRepository repository;

    @Mock
    private IBusEvent busMessage;

    private CreateAccountUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new CreateAccountUseCase(repository, busMessage);
    }

    @Test
    void createAccountSuccessfully() {
        // Arrange
        Account account = new Account("675dbabe03edcf54111957fe", BigDecimal.valueOf(1000), "1234567890", "Juan Perez");
        when(repository.findByAccountNumber("1234567890"))
                .thenReturn(Mono.empty());
        when(repository.save(any(Account.class)))
                .thenReturn(Mono.just(account));

        // Act & Assert
        StepVerifier.create(useCase.apply(account))
                .expectNext(account)
                .verifyComplete();

        // Verify that the bus message was sent
        verify(busMessage).sendMsg(eq("account"), contains("Create account: "));
    }

    @Test
    void createAccountWithExistingNumberShouldFail() {
        // Arrange
        Account existingAccount = new Account("675dbabe03edcf54111957fe", BigDecimal.valueOf(1000), "1234567890", "Juan Perez");
        when(repository.findByAccountNumber("1234567890"))
                .thenReturn(Mono.just(existingAccount));

        Account newAccount = new Account("newAccountId", BigDecimal.valueOf(2000), "1234567890", "Maria Gomez");

        // Act & Assert
        StepVerifier.create(useCase.apply(newAccount))
                .expectError(ConflictException.class)
                .verify();

        // Ensure no save or message sending was triggered
        verify(repository, never()).save(any(Account.class));
        verify(busMessage, never()).sendMsg(anyString(), anyString());
    }
}
