package ec.com.sofka.account.queries;

import ec.com.sofka.NotFoundException;
import ec.com.sofka.account.queries.query.GetAccountByNumberQuery;
import ec.com.sofka.account.queries.responses.AccountResponse;
import ec.com.sofka.account.queries.usecases.GetAccountByNumberViewUseCase;

import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.gateway.dto.AccountDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAccountByNumberUseCaseTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private GetAccountByNumberViewUseCase useCase;

    @Test
    void shouldReturnAccountByNumber() {
        String id = "1";
        String accountNumber = "ACC-001-001";
        BigDecimal balance = BigDecimal.valueOf(100);
        String userId = "user123";

        AccountDTO accountCreated = new AccountDTO(id, accountNumber, balance, userId);

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Mono.just(accountCreated));

        GetAccountByNumberQuery request = new GetAccountByNumberQuery(accountNumber);

        useCase.get(request)
                .as(StepVerifier::create)
                .consumeNextWith(response -> {
                    AccountResponse accountResponse = response.getSingleResult().get();
                    assert accountResponse.getId().equals(id);
                    assert accountResponse.getAccountNumber().equals(accountNumber);
                    assert accountResponse.getBalance().compareTo(balance) == 0;
                    assert accountResponse.getUserId().equals(userId);
                })
                .verifyComplete();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenAccountNotExists() {
        String accountNumber = "ACC-001-001";

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Mono.empty());
        GetAccountByNumberQuery request = new GetAccountByNumberQuery(accountNumber);

        useCase.get(request)
                .as(StepVerifier::create)
                .expectErrorMatches(ex -> ex instanceof NotFoundException && ex.getMessage().equals("Account not found"))
                .verify();
    }
}

