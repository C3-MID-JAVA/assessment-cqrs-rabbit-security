package ec.com.sofka.account;

import ec.com.sofka.account.request.CreateAccountRequest;
import ec.com.sofka.aggregate.customer.events.UserCreated;
import ec.com.sofka.exception.NotFoundException;
import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.generics.domain.DomainEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAccountUseCaseTest {

    @Mock
    private IEventStore eventStore;

    @Mock
    private AccountRepository accountRepository;

    private CreateAccountUseCaseExecute useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new CreateAccountUseCaseExecute(eventStore, accountRepository);
    }

    @Test
    void shouldCreateAccountSuccessfully() {
        String aggregateId = "customer123";
        String userId = "user123";
        String accountNumber = "RANDOM001";

        UserCreated userCreated = new UserCreated(userId, "John Doe", "DOC123");
        userCreated.setAggregateRootId(aggregateId);

        when(eventStore.findAggregate(aggregateId, "customer"))
                .thenReturn(Flux.just(userCreated));

        when(accountRepository.save(any(AccountDTO.class)))
                .thenAnswer(invocation -> {
                    AccountDTO dto = invocation.getArgument(0);
                    dto.setId("accountId123");
                    return Mono.just(dto);
                });

        when(eventStore.save(any(DomainEvent.class)))
                .thenReturn(Mono.empty());

        CreateAccountRequest request = new CreateAccountRequest(aggregateId);

        StepVerifier.create(useCase.execute(request))
                .consumeNextWith(response -> {
                    assert response.getCustomerId().equals(aggregateId);
                    assert response.getAccountNumber().length() == 8; // Verificamos que sea UUID-like
                    assert response.getBalance().compareTo(BigDecimal.ZERO) == 0;
                    assert response.getUserId().equals(userId);
                })
                .verifyComplete();

        verify(eventStore, times(1)).findAggregate(aggregateId, "customer");
        verify(eventStore, atLeastOnce()).save(any(DomainEvent.class));
        verify(accountRepository, times(1)).save(any(AccountDTO.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUserNotFound() {
        String aggregateId = "customer123";

        when(eventStore.findAggregate(aggregateId, "customer"))
                .thenReturn(Flux.empty());

        CreateAccountRequest request = new CreateAccountRequest(aggregateId);

        StepVerifier.create(useCase.execute(request))
                .expectErrorMatches(ex -> ex instanceof NotFoundException && ex.getMessage().equals("User not found"))
                .verify();

        verify(eventStore, times(1)).findAggregate(aggregateId, "customer");
        verify(accountRepository, never()).save(any(AccountDTO.class));
    }

    @Test
    void shouldPropagateErrorWhenEventSaveFails() {
        String aggregateId = "customer123";
        String userId = "user123";

        UserCreated userCreated = new UserCreated(userId, "John Doe", "DOC123");
        userCreated.setAggregateRootId(aggregateId);

        when(eventStore.findAggregate(aggregateId, "customer"))
                .thenReturn(Flux.just(userCreated));

        when(accountRepository.save(any(AccountDTO.class)))
                .thenAnswer(invocation -> {
                    AccountDTO dto = invocation.getArgument(0);
                    dto.setId("accountId123");
                    return Mono.just(dto);
                });

        when(eventStore.save(any(DomainEvent.class)))
                .thenReturn(Mono.error(new RuntimeException("Failed to save event")));

        CreateAccountRequest request = new CreateAccountRequest(aggregateId);

        StepVerifier.create(useCase.execute(request))
                .expectErrorMatches(ex -> ex instanceof RuntimeException && ex.getMessage().equals("Failed to save event"))
                .verify();

        verify(eventStore, times(1)).findAggregate(aggregateId, "customer");
        verify(accountRepository, times(1)).save(any(AccountDTO.class));
    }
}

