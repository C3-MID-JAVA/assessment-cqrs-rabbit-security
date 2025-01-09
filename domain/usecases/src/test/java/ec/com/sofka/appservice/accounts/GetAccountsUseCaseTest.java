package ec.com.sofka.appservice.accounts;

import ec.com.sofka.account.Account;
import ec.com.sofka.appservice.gateway.IAccountRepository;
import ec.com.sofka.appservice.gateway.IBusEvent;
import ec.com.sofka.appservice.queries.usecases.GetAllAccountsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class GetAccountsUseCaseTest {

    @Mock
    private IAccountRepository repository;

    @Mock
    private IBusEvent busMessage;

    private GetAllAccountsUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new GetAllAccountsUseCase(repository, busMessage);
    }

    @Test
    void getAllAccountsSuccessfully() {
        // Arrange
        Account account1 = new Account(
                "675dbabe03edcf54111957fe",
                BigDecimal.valueOf(1000),
                "1234567890",
                "Juan Perez"
        );
        account1.setAccountNumber("123456");

        Account account2 = new Account(
                "675dbabe03edcf54111957ff",
                BigDecimal.valueOf(2000),
                "0987654321",
                "Maria Lopez"
        );
        account2.setAccountNumber("654321");

        List<Account> accounts = Arrays.asList(account1, account2);

        when(repository.findAll())
                .thenReturn(Flux.fromIterable(accounts));

        // Act & Assert
        StepVerifier.create(useCase.apply())
                .expectNext(account1)
                .expectNext(account2)
                .verifyComplete();

        // Verify that a message was sent
        verify(busMessage).sendMsg(eq("account"), eq("found accounts: "));
    }

    @Test
    void getAllAccountsWhenEmpty() {
        // Arrange
        when(repository.findAll())
                .thenReturn(Flux.empty());

        // Act & Assert
        StepVerifier.create(useCase.apply())
                .expectNextCount(0)
                .verifyComplete();

        // Verify that a message was sent
        verify(busMessage).sendMsg(eq("account"), eq("found accounts: "));
    }

    @Test
    void getAllAccountsWithMultipleAccounts() {
        // Arrange
        List<Account> accounts = Arrays.asList(
                createAccount("id1", "Juan", "111111", BigDecimal.valueOf(1000)),
                createAccount("id2", "Maria", "222222", BigDecimal.valueOf(2000)),
                createAccount("id3", "Pedro", "333333", BigDecimal.valueOf(3000)),
                createAccount("id4", "Ana", "444444", BigDecimal.valueOf(4000)),
                createAccount("id5", "Luis", "555555", BigDecimal.valueOf(5000))
        );

        when(repository.findAll())
                .thenReturn(Flux.fromIterable(accounts));

        // Act & Assert
        StepVerifier.create(useCase.apply())
                .recordWith(ArrayList::new)
                .thenConsumeWhile(x -> true)
                .consumeRecordedWith(recordedAccounts -> {
                    assert recordedAccounts.size() == 5;
                    assert recordedAccounts.containsAll(accounts);
                })
                .verifyComplete();

        // Verify that a message was sent
        verify(busMessage).sendMsg(eq("account"), eq("found accounts: "));
    }

    private Account createAccount(String id, String name, String accountNumber, BigDecimal balance) {
        Account account = new Account(id, balance, accountNumber, name);
        account.setAccountNumber(accountNumber);
        return account;
    }
}
