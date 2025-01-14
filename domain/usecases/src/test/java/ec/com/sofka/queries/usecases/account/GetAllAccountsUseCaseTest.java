package ec.com.sofka.queries.usecases.account;

import ec.com.sofka.account.Account;
import ec.com.sofka.gateway.IAccountRepository;
import ec.com.sofka.gateway.dto.account.AccountDTO;
import ec.com.sofka.generics.utils.QueryResponse;
import ec.com.sofka.queries.responses.account.CreateAccountResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class GetAllAccountsUseCaseTest {

    @Mock
    IAccountRepository accountRepository;


    @InjectMocks
    GetAllAccountsUseCase getAllAccountsUseCase;


    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Should retrieve all accounts")
    void apply() {
        AccountDTO account = new AccountDTO();
        account.setAccountNumber("123456789");
        AccountDTO account2 = new AccountDTO();
        account2.setAccountNumber("987654321");

        when(accountRepository.findAllAccounts()).thenReturn(Flux.just(
                account, account2
        ));

        Flux<QueryResponse<CreateAccountResponse>> result = getAllAccountsUseCase.get();
        StepVerifier.create(result)
                .expectNextMatches(queryResponse -> {

                    CreateAccountResponse response1 = queryResponse.getMultipleResults().get(0);
                    assertThat(response1.getAccountNumber()).isEqualTo("123456789");

                    CreateAccountResponse response2 = queryResponse.getMultipleResults().get(1);
                    assertThat(response2.getAccountNumber()).isEqualTo("987654321");

                    return true;
                })
                .expectComplete()
                .verify();

        verify(accountRepository, times(1)).findAllAccounts();
    }

    @Test
    @DisplayName("should retrieve a empty array")
    void apply_returnEmpty() {

        when(accountRepository.findAllAccounts()).thenReturn(Flux.empty());
        Flux<QueryResponse<CreateAccountResponse>> result = getAllAccountsUseCase.get();

        StepVerifier.create(result)
                .expectNextMatches(query  -> query.getMultipleResults().isEmpty())
                .verifyComplete();

        verify(accountRepository, times(1)).findAllAccounts();
    }
}