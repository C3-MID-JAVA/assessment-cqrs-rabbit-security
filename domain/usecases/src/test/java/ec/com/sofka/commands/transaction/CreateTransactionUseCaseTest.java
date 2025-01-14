/*package ec.com.sofka.transaction;

import ec.com.sofka.aggregate.events.TransactionCreated;
import ec.com.sofka.commands.CreateTransactionCommand;
import ec.com.sofka.commands.usecases.CreateTransactionUseCase;
import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.queries.responses.CreateTransactionResponse;
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
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTransactionUseCaseTest {

    @Mock
    private IEventStore eventStore;

    @Mock
    private BusEvent busEvent;

    @InjectMocks
    private CreateTransactionUseCase useCase;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createTransactionSuccessfully() {
        CreateTransactionCommand command = new CreateTransactionCommand(
                BigDecimal.valueOf(500.0), "DEPOSIT", BigDecimal.valueOf(10.0), "accountId"
        );

        DomainEvent existingEvent = mockDomainEventWithAccountNumber();
        when(eventStore.findAggregate(any())).thenReturn(Flux.just(existingEvent));
        when(eventStore.save(any(DomainEvent.class))).thenReturn(Mono.just(new TransactionCreated(
                BigDecimal.valueOf(500.0), "DEPOSIT", BigDecimal.valueOf(10.0), "accountId", "TRANSACTION_ACTIVE"
        )));

        StepVerifier.create(useCase.execute(command))
                .expectNextMatches(response -> {
                    return response.getAmount().equals(BigDecimal.valueOf(500.0))
                            && response.getType().equals("DEPOSIT")
                            && response.getCost().equals(BigDecimal.valueOf(10.0))
                            && response.getIdAccount().equals("accountId")
                            && response.getStatus().equals("TRANSACTION_ACTIVE");
                })
                .verifyComplete();

        verify(eventStore, times(1)).findAggregate(any());
        verify(eventStore, times(1)).save(any(DomainEvent.class));
        verify(busEvent, times(1)).sendEvent(any(DomainEvent.class));
    }

    @Test
    void createTransactionFailsWhenAccountDoesNotExist() {
        CreateTransactionCommand command = new CreateTransactionCommand(
                BigDecimal.valueOf(500.0), "DEPOSIT", BigDecimal.valueOf(10.0), "accountId"
        );

        when(eventStore.findAggregate(any())).thenReturn(Flux.empty());

        StepVerifier.create(useCase.execute(command))
                .expectErrorMessage("Account does not exist in the repository")
                .verify();

        verify(eventStore, times(1)).findAggregate(any());
        verify(eventStore, never()).save(any(DomainEvent.class));
        verify(busEvent, never()).sendEvent(any(DomainEvent.class));
    }

    private DomainEvent mockDomainEventWithAccountNumber() {
        TransactionCreated event = new TransactionCreated(
                BigDecimal.valueOf(1000.0), "DEPOSIT", BigDecimal.valueOf(10.0), "accountId", "TRANSACTION_ACTIVE"
        );
        event.setAggregateRootId("aggregateId");
        return event;
    }
}
*/
