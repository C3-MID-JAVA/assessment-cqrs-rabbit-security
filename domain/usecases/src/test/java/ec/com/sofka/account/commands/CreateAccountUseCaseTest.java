package ec.com.sofka.account.commands;

import ec.com.sofka.NotFoundException;
import ec.com.sofka.account.commands.usecases.CreateAccountUseCase;
import ec.com.sofka.aggregate.customer.events.UserCreated;
import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    private BusEvent busEvent;

    @InjectMocks
    private CreateAccountUseCase useCase;

    @Test
    void shouldCreateAccountSuccessfully() {
        String aggregateId = "customer123";
        String userId = "user123";

        UserCreated userCreated = new UserCreated(userId, "John Doe", "DOC123");
        userCreated.setAggregateRootId(aggregateId);

        when(eventStore.findAggregate(aggregateId, "customer"))
                .thenReturn(Flux.just(userCreated));

        when(eventStore.save(any(DomainEvent.class)))
                .thenReturn(Mono.empty());

        CreateAccountCommand request = new CreateAccountCommand(aggregateId);

        StepVerifier.create(useCase.execute(request))
                .consumeNextWith(response -> {
                    assert response.getCustomerId().equals(aggregateId);
                    assert response.getAccountNumber().length() == 8;
                    assert response.getBalance().compareTo(BigDecimal.ZERO) == 0;
                    assert response.getUserId().equals(userId);
                })
                .verifyComplete();

        verify(eventStore, times(1)).findAggregate(aggregateId, "customer");
        verify(eventStore, atLeastOnce()).save(any(DomainEvent.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUserNotFound() {
        String aggregateId = "customer123";

        when(eventStore.findAggregate(aggregateId, "customer"))
                .thenReturn(Flux.empty());

        CreateAccountCommand request = new CreateAccountCommand(aggregateId);

        StepVerifier.create(useCase.execute(request))
                .expectErrorMatches(ex -> ex instanceof NotFoundException && ex.getMessage().equals("User not found"))
                .verify();

        verify(eventStore, times(1)).findAggregate(aggregateId, "customer");
    }
}

