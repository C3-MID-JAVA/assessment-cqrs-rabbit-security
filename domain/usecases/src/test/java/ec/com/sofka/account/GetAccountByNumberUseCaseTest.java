package ec.com.sofka.account;

import ec.com.sofka.account.request.GetAccountByNumberRequest;
import ec.com.sofka.aggregate.customer.events.AccountCreated;
import ec.com.sofka.exception.NotFoundException;
import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.gateway.IEventStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAccountByNumberUseCaseTest {

    @Mock
    private IEventStore repository;

    @Mock
    private AccountRepository accountRepository;

    private GetAccountByNumberUseCaseExecute useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new GetAccountByNumberUseCaseExecute(accountRepository, repository);
    }

    @Test
    void shouldReturnAccountByNumber() {
        String aggregateId = "customer123";
        String userId = "user123";
        String accountNumber = "ACC-001-001";

        AccountCreated accountCreated = new AccountCreated(
                "account1",
                accountNumber,
                new BigDecimal("1500.00"),
                userId
        );
        accountCreated.setAggregateRootId(aggregateId);

        when(repository.findAggregate(aggregateId, "customer"))
                .thenReturn(Flux.just(accountCreated));

        GetAccountByNumberRequest request = new GetAccountByNumberRequest(aggregateId, accountNumber);

        useCase.execute(request)
                .as(StepVerifier::create)
                .consumeNextWith(response -> {
                    assert response.getCustomerId().equals(aggregateId);
                    assert response.getAccountNumber().equals(accountNumber);
                    assert response.getBalance().compareTo(new BigDecimal("1500.00")) == 0;
                    assert response.getUserId().equals(userId);
                })
                .verifyComplete();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenAccountNotExists() {
        String aggregateId = "customer123";
        String userId = "user123";
        String accountNumber = "ACC-001-001";

        AccountCreated accountCreated = new AccountCreated(
                "account2",
                "ACC-002-002",
                new BigDecimal("1000.00"),
                userId
        );
        accountCreated.setAggregateRootId(aggregateId);

        when(repository.findAggregate(aggregateId, "customer"))
                .thenReturn(Flux.just(accountCreated));

        GetAccountByNumberRequest request = new GetAccountByNumberRequest(aggregateId, accountNumber);

        useCase.execute(request)
                .as(StepVerifier::create)
                .expectErrorMatches(ex -> ex instanceof NotFoundException && ex.getMessage().equals("Account not found"))
                .verify();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenNoEventsExist() {
        String aggregateId = "customer123";
        String accountNumber = "ACC-001-001";

        when(repository.findAggregate(aggregateId, "customer"))
                .thenReturn(Flux.empty());

        GetAccountByNumberRequest request = new GetAccountByNumberRequest(aggregateId, accountNumber);

        useCase.execute(request)
                .as(StepVerifier::create)
                .expectErrorMatches(ex -> ex instanceof NotFoundException && ex.getMessage().equals("Account not found"))
                .verify();
    }
}

