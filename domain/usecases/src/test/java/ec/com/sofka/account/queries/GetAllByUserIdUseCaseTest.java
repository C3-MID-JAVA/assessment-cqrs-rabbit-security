package ec.com.sofka.account.queries;

import ec.com.sofka.account.queries.query.GetAllByUserIdQuery;
import ec.com.sofka.account.queries.responses.AccountResponse;
import ec.com.sofka.account.queries.usecases.GetAllByUserIdViewUseCase;
import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.gateway.dto.AccountDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllByUserIdUseCaseTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private GetAllByUserIdViewUseCase useCase;

    @Test
    void shouldReturnAllAccountsForUser() {
        String userId = "user123";
        String accountNumber1 = "ACC-001-001";
        String accountNumber2 = "ACC-002-002";

        AccountDTO accountCreated = new AccountDTO(
                "1",
                accountNumber1,
                new BigDecimal("1000.00"),
                userId
        );

        AccountDTO accountCreated2 = new AccountDTO(
                "2",
                accountNumber2,
                new BigDecimal("2000.00"),
                userId
        );

        when(accountRepository.getAllByUserId(userId)).thenReturn(Flux.just(accountCreated, accountCreated2));

        GetAllByUserIdQuery request = new GetAllByUserIdQuery(userId);

        useCase.get(request)
                .as(StepVerifier::create)
                .consumeNextWith(response -> {
                    List<AccountResponse> accountResponse = response.getMultipleResults();
                    assert accountResponse.get(0).getId().equals("1");
                    assert accountResponse.get(0).getAccountNumber().equals(accountNumber1);
                    assert accountResponse.get(0).getBalance().compareTo(new BigDecimal("1000.00")) == 0;
                    assert accountResponse.get(0).getUserId().equals(userId);
                    assert accountResponse.get(1).getId().equals("2");
                    assert accountResponse.get(1).getAccountNumber().equals(accountNumber2);
                    assert accountResponse.get(1).getBalance().compareTo(new BigDecimal("2000.00")) == 0;
                    assert accountResponse.get(1).getUserId().equals(userId);
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyFluxWhenNoAccounts() {
        String userId = "user123";

        when(accountRepository.getAllByUserId(userId)).thenReturn(Flux.empty());

        GetAllByUserIdQuery request = new GetAllByUserIdQuery(userId);

        StepVerifier.create(useCase.get(request))
                .expectNextMatches(Objects::nonNull)
                .verifyComplete();
    }

}