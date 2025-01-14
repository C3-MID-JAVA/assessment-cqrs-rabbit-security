package ec.com.sofka.commands.account;

import ec.com.sofka.aggregate.events.AccountUpdated;
import ec.com.sofka.commands.UpdateAccountCommand;
import ec.com.sofka.commands.usecases.UpdateAccountUseCase;
import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
class UpdateAccountUseCaseTest {

    @Mock
    private IEventStore eventStore;

    @Mock
    private BusEvent busEvent;

    @InjectMocks
    private UpdateAccountUseCase useCase;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void updateAccountSuccessfully() {
        UpdateAccountCommand command = new UpdateAccountCommand(
                "aggregateId", BigDecimal.valueOf(2000.0), "123456789", "John Doe", "Active", "userId"
        );

        DomainEvent existingEvent = mockDomainEventWithAccountNumber();
        when(eventStore.findAggregate(any())).thenReturn(Flux.just(existingEvent));
        when(eventStore.save(any(DomainEvent.class))).thenReturn(Mono.just(new AccountUpdated(
                "accountId", BigDecimal.valueOf(2000.0), "123456789", "John Doe", "Active", "userId"
        )));

        StepVerifier.create(useCase.execute(command))
                .expectNextMatches(response -> {
                    return response.getAccountNumber().equals("123456789")
                            && response.getName().equals("John Doe")
                            && response.getStatus().equals("Active")
                            && response.getIdUser().equals("userId");
                })
                .verifyComplete();

        verify(eventStore, times(1)).findAggregate(any());
        verify(eventStore, times(1)).save(any(DomainEvent.class));
        verify(busEvent, times(1)).sendEvent(any(DomainEvent.class));
    }

    @Test
    void updateAccountFailsWhenAccountDoesNotExist() {
        UpdateAccountCommand command = new UpdateAccountCommand(
                "aggregateId", BigDecimal.valueOf(2000.0), "123456789", "John Doe", "Active", "userId"
        );

        when(eventStore.findAggregate(any())).thenReturn(Flux.empty());

        StepVerifier.create(useCase.execute(command))
                .expectErrorMessage("Cuenta no existe en el repositorio")
                .verify();

        verify(eventStore, times(1)).findAggregate(any());
        verify(eventStore, never()).save(any(DomainEvent.class));
        verify(busEvent, never()).sendEvent(any(DomainEvent.class));
    }

    private DomainEvent mockDomainEventWithAccountNumber() {
        AccountUpdated event = new AccountUpdated(
                "accountId", BigDecimal.valueOf(1000.0), "123456789", "John Doe", "Active", "userId"
        );
        event.setAggregateRootId("aggregateId");
        return event;
    }
}