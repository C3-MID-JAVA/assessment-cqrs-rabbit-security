package ec.com.sofka.account;

import ec.com.sofka.account.request.GetAllByUserIdRequest;
import ec.com.sofka.aggregate.customer.events.AccountCreated;
import ec.com.sofka.aggregate.customer.events.UserCreated;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllByUserIdUseCaseTest {

    @Mock
    private IEventStore repository;

    private GetAllByUserIdUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new GetAllByUserIdUseCase(repository);
    }

    @Test
    void shouldReturnAllAccountsForUser() {
        String aggregateId = "customer123";
        String userId = "user123";
        String accountNumber1 = "ACC-001-001";
        String accountNumber2 = "ACC-002-002";

        AccountCreated account1Created = new AccountCreated(
                "account1",
                accountNumber1,
                new BigDecimal("1000.00"),
                userId
        );
        account1Created.setAggregateRootId(aggregateId);

        AccountCreated account2Created = new AccountCreated(
                "account2",
                accountNumber2,
                new BigDecimal("2000.00"),
                userId
        );
        account2Created.setAggregateRootId(aggregateId);

        List<DomainEvent> events = List.of(account1Created, account2Created);

        when(repository.findAggregate(aggregateId, "customer"))
                .thenReturn(Flux.fromIterable(events));

        GetAllByUserIdRequest request = new GetAllByUserIdRequest(aggregateId);

        useCase.execute(request)
                .as(StepVerifier::create)
                .consumeNextWith(response -> {
                    assert response.getCustomerId().equals(aggregateId);
                    assert response.getAccountNumber().equals(accountNumber1);
                    assert response.getBalance().compareTo(new BigDecimal("1000.00")) == 0;
                    assert response.getUserId().equals(userId);
                })
                .consumeNextWith(response -> {
                    assert response.getCustomerId().equals(aggregateId);
                    assert response.getAccountNumber().equals(accountNumber2);
                    assert response.getBalance().compareTo(new BigDecimal("2000.00")) == 0;
                    assert response.getUserId().equals(userId);
                })
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyFluxWhenNoAccounts() {
        String aggregateId = "customer123";
        String userId = "user123";

        DomainEvent userCreated = new UserCreated(userId, "John Doe", "DOC123");

        userCreated.setAggregateRootId(aggregateId);
        List<DomainEvent> events = List.of(userCreated);

        when(repository.findAggregate(aggregateId, "customer"))
                .thenReturn(Flux.fromIterable(events));

        GetAllByUserIdRequest request = new GetAllByUserIdRequest(aggregateId);

        StepVerifier.create(useCase.execute(request))
                .verifyComplete();
    }

}