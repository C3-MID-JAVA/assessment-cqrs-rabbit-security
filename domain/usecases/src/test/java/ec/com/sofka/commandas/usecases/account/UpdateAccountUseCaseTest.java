package ec.com.sofka.commandas.usecases.account;

import static org.mockito.Mockito.*;

import ec.com.sofka.aggregates.account.events.AccountUpdated;
import ec.com.sofka.commands.AccountCommand;
import ec.com.sofka.commands.usecases.account.UpdateAccountUseCase;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.busMessage.EventBusMessage;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.utils.enums.StatusEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class UpdateAccountUseCaseTest {
    @Mock
    private IEventStore eventStore;

    @Mock
    private EventBusMessage eventBusMessage;

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
        AccountCommand command = new AccountCommand( "aggregateId", "1234567890", new BigDecimal(100), StatusEnum.ACTIVE, "123" );

        DomainEvent existingEvent = mockDomainEvent();
        when(eventStore.findAggregate(any())).thenReturn(Flux.just(existingEvent));
        when(eventStore.save(any(DomainEvent.class))).thenReturn(Mono.just(new AccountUpdated("aggregateId", "1234567890", new BigDecimal(100), StatusEnum.ACTIVE, null)));

        StepVerifier.create(useCase.execute(command))
                .expectNextMatches(response -> {
                    return response.getNumber().equals("1234567890")
                            && response.getBalance().equals(new BigDecimal(100))
                            && response.getStatus().name().equals("ACTIVE");
                })
                .verifyComplete();

        verify(eventStore, times(1)).findAggregate(any());
        verify(eventStore, times(1)).save(any(DomainEvent.class));
        verify(eventBusMessage, times(1)).sendEvent(any(DomainEvent.class));
    }

    @Test
    void updateAccountFailsWhenAccountDoesNotExist() {
        AccountCommand command = new AccountCommand( "aggregateId", "1234567890", new BigDecimal(100), StatusEnum.ACTIVE, "123" );

        when(eventStore.findAggregate(any())).thenReturn(Flux.empty());

        StepVerifier.create(useCase.execute(command))
                .expectErrorMessage("Account not found")
                .verify();

        verify(eventStore, times(1)).findAggregate(any());
        verify(eventStore, never()).save(any(DomainEvent.class));
    }

    private DomainEvent mockDomainEvent() {
        AccountUpdated event = new AccountUpdated("accountId", "1234567890", new BigDecimal(10), StatusEnum.ACTIVE, null);
        event.setAggregateRootId("aggregateId");
        return event;
    }

}