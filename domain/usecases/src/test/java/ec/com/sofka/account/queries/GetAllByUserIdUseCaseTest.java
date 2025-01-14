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
        String userId = "user789";
        String accountNumber1 = "ACC-111-111";
        String accountNumber2 = "ACC-222-222";

        AccountDTO accountCreated = new AccountDTO(
                "3",
                accountNumber1,
                new BigDecimal("1500.00"),
                userId
        );

        AccountDTO accountCreated2 = new AccountDTO(
                "4",
                accountNumber2,
                new BigDecimal("2500.00"),
                userId
        );

        when(accountRepository.getAllByUserId(userId)).thenReturn(Flux.just(accountCreated, accountCreated2));

        GetAllByUserIdQuery request = new GetAllByUserIdQuery(userId);

        useCase.get(request)
                .as(StepVerifier::create)
                .consumeNextWith(response -> {
                    List<AccountResponse> accountResponse = response.getMultipleResults();
                    assert accountResponse.get(0).getId().equals("3");
                    assert accountResponse.get(0).getAccountNumber().equals(accountNumber1);
                    assert accountResponse.get(0).getBalance().compareTo(new BigDecimal("1500.00")) == 0;
                    assert accountResponse.get(0).getUserId().equals(userId);
                    assert accountResponse.get(1).getId().equals("4");
                    assert accountResponse.get(1).getAccountNumber().equals(accountNumber2);
                    assert accountResponse.get(1).getBalance().compareTo(new BigDecimal("2500.00")) == 0;
                    assert accountResponse.get(1).getUserId().equals(userId);
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyFluxWhenNoAccounts() {
        String userId = "user789";

        when(accountRepository.getAllByUserId(userId)).thenReturn(Flux.empty());

        GetAllByUserIdQuery request = new GetAllByUserIdQuery(userId);

        StepVerifier.create(useCase.get(request))
                .expectNextMatches(Objects::nonNull)
                .verifyComplete();
    }


}