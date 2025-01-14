package ec.com.sofka.commands.usecases;

import ec.com.sofka.account.Account;
import ec.com.sofka.account.values.AccountId;
import ec.com.sofka.account.values.objects.AccountNumber;
import ec.com.sofka.account.values.objects.AccountType;
import ec.com.sofka.account.values.objects.Balance;
import ec.com.sofka.account.values.objects.OwnerName;
import ec.com.sofka.aggregate.account.events.CardCreated;
import ec.com.sofka.commands.CreateCardCommand;
import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.gateway.ICardRepository;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.utils.QueryResponse;
import ec.com.sofka.queries.query.GetAccountQuery;
import ec.com.sofka.queries.responses.account.CreateAccountResponse;
import ec.com.sofka.queries.responses.account.GetAccountByNumResponse;
import ec.com.sofka.queries.responses.card.CreateCardResponse;
import ec.com.sofka.queries.usecases.account.GetAccountByAccountNumberUseCase;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCardUseCaseTest {

    @Mock
    private IEventStore eventStore;

    @Mock
    private ICardRepository cardRepository;

    @Mock
    private GetAccountByAccountNumberUseCase getAccountByAccountNumberUseCase;

    @Mock
    private BusEvent busEvent;

    @InjectMocks
    private CreateCardUseCase useCase;

    private CreateCardCommand command;

    @BeforeEach
    void setUp() {

        command = new CreateCardCommand(
                "customerId", "Test Card", "1234567890123456",
                "Credit", "Active", "12/30",
                null,
                BigDecimal.valueOf(5000.00),
                "John Doe",
                null
        );
    }

    @Test
    void createCardSuccessfully() {

        when(eventStore.findAggregate("customerId")).thenReturn(Flux.empty());
        when(getAccountByAccountNumberUseCase.get(any(GetAccountQuery.class)))
                .thenReturn(Mono.just(mockAccountResponse()));
        when(cardRepository.existsByCardCVV(anyString())).thenReturn(Mono.just(false));


        StepVerifier.create(useCase.execute(command))
                .expectNextMatches(response ->
                        response.getCardNumber().equals("1234567890123456") &&
                                response.getCardName().equals("Test Card") &&
                                response.getCardHolderName().equals("John Doe") &&
                                response.getCardLimit().equals(BigDecimal.valueOf(5000.00))
                )
                .verifyComplete();


        verify(eventStore, times(1)).findAggregate("customerId");
        verify(busEvent, times(1)).sendEventCardCreated(any());
    }

    @Test
    void createCardFailsWhenCardExists() {

        DomainEvent existingEvent = mockDomainEventWithCardNumber();
        when(eventStore.findAggregate("customerId")).thenReturn(Flux.just(existingEvent));


        StepVerifier.create(useCase.execute(command))
                .expectErrorMessage("Card already exists")
                .verify();


        verify(eventStore, times(1)).findAggregate("customerId");
        verify(busEvent, never()).sendEventCardCreated(any());
    }

    @Test
    void createCardFailsWhenAccountDoesNotExist() {


        when(eventStore.findAggregate("customerId")).thenReturn(Flux.empty());
        when(getAccountByAccountNumberUseCase.get(any(GetAccountQuery.class)))
                .thenReturn(Mono.empty());


        StepVerifier.create(useCase.execute(command))
                .expectErrorMessage("Account does not exist")
                .verify();


        verify(eventStore, times(1)).findAggregate("customerId");
        verify(busEvent, never()).sendEventCardCreated(any());
    }

    private DomainEvent mockDomainEventWithCardNumber() {
        CardCreated event = new CardCreated(
                "cardId", "Test Card", "1234567890123456",
                "Credit", "Active", "12/30", "1234",
                BigDecimal.valueOf(5000.00), "John Doe", new Account(
                AccountId.of("accountId"),
                Balance.of(BigDecimal.valueOf(1000.00)),
                AccountNumber.of("123456789"),
                OwnerName.of("John Doe"),
                AccountType.of("Savings")
        )
        );
        event.setAggregateRootId("customerId");
        return event;
    }

    private QueryResponse<GetAccountByNumResponse> mockAccountResponse() {
        Account mockAccount = new Account(
                AccountId.of("accountId"),
                Balance.of(BigDecimal.valueOf(1000.00)),
                AccountNumber.of("123456789"),
                OwnerName.of("John Doe"),
                AccountType.of("Savings")
        );

        CreateAccountResponse createAccountResponse = new CreateAccountResponse(
                "accountId",
                "123456",
                "John Doe",
                "Savings",
                BigDecimal.valueOf(1000.00)
        );

        GetAccountByNumResponse getAccountResponse = new GetAccountByNumResponse(mockAccount, createAccountResponse);
        return QueryResponse.ofSingle(getAccountResponse);
    }

}