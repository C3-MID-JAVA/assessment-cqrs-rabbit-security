package ec.com.sofka.queries.query.account;

import ec.com.sofka.aggregates.account.entities.account.Account;
import ec.com.sofka.aggregates.account.entities.account.values.AccountId;
import ec.com.sofka.aggregates.account.entities.account.values.objects.AccountNumber;
import ec.com.sofka.aggregates.account.values.objects.Amount;
import ec.com.sofka.exceptions.RecordNotFoundException;
import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.generics.shared.PropertyQuery;
import ec.com.sofka.generics.shared.QueryResponse;
import ec.com.sofka.mapper.AccountMapper;
import ec.com.sofka.queries.responses.AccountResponse;
import ec.com.sofka.utils.enums.StatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAccountByNumberUseCaseTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private FindAccountByNumberUseCase findAccountByNumberUseCase;

    @Mock
    private ErrorBusMessage errorBusMessage;

    private Account account;
    private PropertyQuery propertyQuery;
    @BeforeEach
    void setUp() {
        account = new Account(AccountId.of("1"), AccountNumber.of("2200000000"), Amount.of(new BigDecimal(100)), StatusEnum.ACTIVE, null);
        propertyQuery = new PropertyQuery(account.getAccountNumber().getValue());
    }

    @Test
    @DisplayName("When finding account with valid number should return account response")
    void findExistingAccountTest() {
        when(accountRepository.findByNumber(eq(propertyQuery.getProperty())))
                .thenReturn(Mono.just(AccountMapper.mapToDTOFromModel(account)));

        Mono<QueryResponse<AccountResponse>> result = findAccountByNumberUseCase.get(propertyQuery);

        StepVerifier.create(result)
                .expectNextMatches(response -> {
                    AccountResponse accountResponse = response.getSingleResult()
                            .orElseThrow(() -> new RecordNotFoundException("Account not found"));
                    return accountResponse.getNumber().equals(account.getAccountNumber().getValue())
                            && accountResponse.getStatus().name().equals(account.getStatus().name())
                            && accountResponse.getBalance().equals(account.getBalance().getValue());
                })
                .verifyComplete();

        verify(accountRepository).findByNumber(eq(propertyQuery.getProperty()));
        verifyNoInteractions(errorBusMessage);
    }

    @Test
    @DisplayName("When finding non-existing account should send error message and throw exception")
    void findNonExistingAccountTest() {
        when(accountRepository.findByNumber(propertyQuery.getProperty())).thenReturn(Mono.empty());

        Mono<QueryResponse<AccountResponse>> result = findAccountByNumberUseCase.get(propertyQuery);

        StepVerifier.create(result)
                .expectError(RecordNotFoundException.class)
                .verify();

        verify(accountRepository).findByNumber(account.getAccountNumber().getValue());
        verify(errorBusMessage).sendMsg(argThat(errorMessage ->
                errorMessage.getError().equals("Account not found") &&
                        errorMessage.getProcess().equals("Get Account by NumberAccount")));
    }

}