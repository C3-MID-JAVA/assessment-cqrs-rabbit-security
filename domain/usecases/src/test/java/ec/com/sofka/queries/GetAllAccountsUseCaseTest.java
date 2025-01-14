package ec.com.sofka.queries;

import ec.com.sofka.aggregate.events.AccountCreated;
import ec.com.sofka.queries.query.GetAccountQuery;
import ec.com.sofka.queries.responses.GetAccountResponse;
import ec.com.sofka.queries.usecases.GetAllAccountsUseCase;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.utils.QueryResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllAccountsUseCaseTest {

    @Mock
    private IEventStore eventStore;

    @InjectMocks
    private GetAllAccountsUseCase useCase;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }



    @Test
    void getAllAccountsWhenNoAccountsExist() {
        when(eventStore.findAllAggregates()).thenReturn(Flux.empty());

        StepVerifier.create(useCase.get(new GetAccountQuery()))
                .expectNextMatches(response -> response.getMultipleResults().isEmpty())
                .verifyComplete();

        verify(eventStore, times(1)).findAllAggregates();
    }
}