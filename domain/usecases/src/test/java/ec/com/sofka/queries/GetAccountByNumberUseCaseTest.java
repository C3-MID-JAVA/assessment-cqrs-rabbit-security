/*package ec.com.sofka.queries;

import ec.com.sofka.CuentaNoEncontradaException;
import ec.com.sofka.aggregate.events.AccountCreated;
import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.queries.query.GetAccountQuery;
import ec.com.sofka.queries.responses.GetAccountResponse;
import ec.com.sofka.queries.usecases.GetAccountByNumberUseCase;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.utils.QueryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetAccountByNumberUseCaseTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private IEventStore eventStore;

    @InjectMocks
    private GetAccountByNumberUseCase getAccountByNumberUseCase;

    private DomainEvent event;

    @BeforeEach
    void setUp() {
        event = new AccountCreated("accountId1", "123456789", BigDecimal.valueOf(1000.0), "John Doe", "Active", "userId1");
    }

    @Test
    @DisplayName("Should retrieve account by number when account exists")
    public void testGetAccountByNumber_WhenAccountExists() {
        when(eventStore.findAggregate("accountId1")).thenReturn(Flux.just(event));
        when(accountRepository.findByNumber("123456789")).thenReturn(Mono.just(new AccountDTO("accountId1", "John Doe", "123456789", BigDecimal.valueOf(1000.0), "Active", "userId1")));

        Mono<QueryResponse<GetAccountResponse>> result = getAccountByNumberUseCase.get(new GetAccountQuery("accountId1", "123456789"));

        StepVerifier.create(result)
                .expectNextMatches(queryResponse -> {
                    GetAccountResponse response = queryResponse.getSingleResult().orElse(null);
                    assert response != null;
                    assertThat(response.getAccountNumber()).isEqualTo("123456789");
                    assertThat(response.getName()).isEqualTo("John Doe");
                    assertThat(response.getBalance()).isEqualTo(BigDecimal.valueOf(1000.0));
                    assertThat(response.getStatus()).isEqualTo("Active");
                    return true;
                })
                .verifyComplete();

        verify(eventStore).findAggregate("accountId1");
        verify(accountRepository).findByNumber("123456789");
    }

    @Test
    @DisplayName("Should throw exception when account does not exist")
    public void testGetAccountByNumber_WhenAccountDoesNotExist() {
        when(eventStore.findAggregate("accountId1")).thenReturn(Flux.just(event));
        when(accountRepository.findByNumber("123456789")).thenReturn(Mono.empty());

        Mono<QueryResponse<GetAccountResponse>> result = getAccountByNumberUseCase.get(new GetAccountQuery("accountId1", "123456789"));

        StepVerifier.create(result)
                .expectError(CuentaNoEncontradaException.class)
                .verify();

        verify(eventStore).findAggregate("accountId1");
        verify(accountRepository).findByNumber("123456789");
    }
}
*/
