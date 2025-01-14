package ec.com.sofka.commands.usecases;

import ec.com.sofka.account.Account;
import ec.com.sofka.account.values.AccountId;
import ec.com.sofka.account.values.objects.AccountNumber;
import ec.com.sofka.account.values.objects.AccountType;
import ec.com.sofka.account.values.objects.Balance;
import ec.com.sofka.account.values.objects.OwnerName;
import ec.com.sofka.aggregate.account.events.AccountCreated;
import ec.com.sofka.aggregate.account.events.CardCreated;
import ec.com.sofka.aggregate.transaction.events.TransactionCreated;
import ec.com.sofka.commands.CreateCardCommand;
import ec.com.sofka.commands.transaction.AccountDepositCommand;
import ec.com.sofka.commands.transaction.AtmTransactionCommand;
import ec.com.sofka.commands.transaction.TransactionCommand;
import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.dto.account.AccountDTO;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.transaction.values.TransactionId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    void processAccountDepositTransaction_Success() {

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountNumber("123456789");
        AccountDepositCommand command = new AccountDepositCommand(
                null,
                null,
                "customerId",
                "description",
                BigDecimal.valueOf(100.0),
                "BA",
                BigDecimal.valueOf(2.0),
                null,
                null,
                accountDTO,
                "receiverCustomerId"
        );

        DomainEvent senderEvent = mockDomainEventWithAccountNumber("customerId");
        DomainEvent receiverEvent = mockDomainEventWithAccountNumber("receiverCustomerId");


        when(eventStore.findAggregate(command.getCustomerId())).thenReturn(Flux.just(senderEvent));
        when(eventStore.findAggregate(command.getReceiverCustomerId())).thenReturn(Flux.just(receiverEvent));
        doNothing().when(busEvent).sendEventTransactionCreated(any());


        StepVerifier.create(useCase.execute(command))
                .expectNextMatches(response -> response.getTransactionType().equals("BA") &&
                        response.getAmount().compareTo(BigDecimal.valueOf(100.0)) == 0 &&
                        response.getTransactionFee().compareTo(BigDecimal.valueOf(2.0)) == 0)
                .verifyComplete();


        verify(eventStore, times(2)).findAggregate(anyString());
        verify(busEvent, atLeastOnce()).sendEventTransactionCreated(any());
    }

    @Test
    void processATMTransaction_Success() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountNumber("123456789");
        AtmTransactionCommand command = new AtmTransactionCommand(
                null,
                null,
                "customerId",
                "description",
                BigDecimal.valueOf(100.0),
                "ATM",
                BigDecimal.valueOf(2.0),
                null,
                new CreateCardCommand(
                        "customerId",
                        "MyCard",
                        "1234567890123456",
                        "VISA",
                        "ACTIVE",
                        "12/25",
                        "123",
                        BigDecimal.valueOf(10000.00),
                        "John Doe",
                        accountDTO
                ),
                "ATM NAME",
                "DEPOSIT"

        );
        DomainEvent senderEvent = mockDomainEventWithAccountNumber("customerId");
        DomainEvent cardEvent = mockDomainEventWithCardNumber();


        when(eventStore.findAggregate(command.getCustomerId())).thenReturn(Flux.just(senderEvent, cardEvent));
        doNothing().when(busEvent).sendEventTransactionCreated(any());


        StepVerifier.create(useCase.execute(command))
                .expectNextMatches(response -> response.getTransactionType().equals("ATM") &&
                        response.getAmount().compareTo(BigDecimal.valueOf(100.0)) == 0 &&
                        response.getTransactionFee().compareTo(BigDecimal.valueOf(2.0)) == 0)
                .verifyComplete();


        verify(eventStore, times(1)).findAggregate(anyString());
        verify(busEvent, atLeastOnce()).sendEventTransactionCreated(any());
    }

    @Test
    void processAccountDepositTransaction_ReceiverAccountNotFound() {

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountNumber("123456789");
        AccountDepositCommand command = new AccountDepositCommand(
                null,
                null,
                "customerId",
                "description",
                BigDecimal.valueOf(100.0),
                "BA",
                BigDecimal.valueOf(2.0),
                null,
                null,
                accountDTO,
                "receiverCustomerId"
        );

        DomainEvent senderEvent = mockDomainEventWithAccountNumber("customerId");
        DomainEvent receiverEvent = mockDomainEventWithAccountNumber("receiverCustomerId");


        when(eventStore.findAggregate(command.getCustomerId())).thenReturn(Flux.just(senderEvent));
        when(eventStore.findAggregate(command.getReceiverCustomerId())).thenReturn(Flux.empty());



        StepVerifier.create(useCase.execute(command))
                .expectErrorMessage("Receiver account not found")
                .verify();


        verify(eventStore, times(2)).findAggregate(anyString());
        verify(busEvent, never()).sendEventTransactionCreated(any());
    }

    @Test
    void processAccountDepositTransaction_SenderAccountNotFound() {

        AccountDepositCommand command = new AccountDepositCommand(
                null,
                null,
                "customerId",
                "description",
                BigDecimal.valueOf(100.0),
                "BA",
                BigDecimal.valueOf(2.0),
                null,
                null,
                null,
                "receiverCustomerId"
        );

        when(eventStore.findAggregate(command.getCustomerId())).thenReturn(Flux.empty());


        StepVerifier.create(useCase.execute(command))
                .expectErrorMessage("Sender account not found")
                .verify();


        verify(eventStore, times(2)).findAggregate(anyString());
        verify(busEvent, never()).sendEventTransactionCreated(any());
    }

    @Test
    void processTransaction_CardNotFound() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountNumber("123456789");
        AtmTransactionCommand command = new AtmTransactionCommand(
                null,
                null,
                "customerId",
                "description",
                BigDecimal.valueOf(100.0),
                "ATM",
                BigDecimal.valueOf(2.0),
                null,
                new CreateCardCommand(
                        "customerId",
                        "MyCard",
                        "1234567890123456",
                        "VISA",
                        "ACTIVE",
                        "12/25",
                        "123",
                        BigDecimal.valueOf(10000.00),
                        "John Doe",
                        accountDTO
                ),
                "ATM NAME",
                "DEPOSIT"

        );

        when(eventStore.findAggregate(command.getCustomerId())).thenReturn(Flux.empty());


        StepVerifier.create(useCase.execute(command))
                .expectErrorMessage("Getting card failed")
                .verify();


        verify(eventStore, times(1)).findAggregate(anyString());
        verify(busEvent, never()).sendEventTransactionCreated(any());
    }

    @Test
    void processTransaction_Account_CardNotFound() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setAccountNumber("123456789");
        AtmTransactionCommand command = new AtmTransactionCommand(
                null,
                null,
                "customerId",
                "description",
                BigDecimal.valueOf(100.0),
                "ATM",
                BigDecimal.valueOf(2.0),
                null,
                new CreateCardCommand(
                        "customerId",
                        "MyCard",
                        "1234567890123456",
                        "VISA",
                        "ACTIVE",
                        "12/25",
                        "123",
                        BigDecimal.valueOf(10000.00),
                        "John Doe",
                        accountDTO
                ),
                "ATM NAME",
                "DEPOSIT"

        );




        DomainEvent senderEvent = mockDomainEventWithAccountNumber("customerId");
        DomainEvent cardEvent = mockDomainEventWithCardNumber();


        when(eventStore.findAggregate(command.getCustomerId())).thenReturn(Flux.just(cardEvent));

        StepVerifier.create(useCase.execute(command))
                .expectErrorMessage("Account or Card not found in store")
                .verify();


        verify(eventStore, times(1)).findAggregate(anyString());
        verify(busEvent, never()).sendEventTransactionCreated(any());
    }


    private DomainEvent mockDomainEventWithAccountNumber(String customerId) {
        AccountCreated event = new AccountCreated(
                null,
                "123456789",
                BigDecimal.valueOf(1000.00),
                "John Doe",
                "Savings");

        event.setAggregateRootId(customerId);
        event.setAggregateRootName("customer");
        return event;
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

    // Métodos auxiliares para eventos simulados
    private DomainEvent mockSenderEvent() {
        return new TransactionCreated(
                new TransactionId().getValue(),
                "description",
                BigDecimal.valueOf(100.0),
                "BA",
                BigDecimal.valueOf(2.0),
                LocalDateTime.now(),
                new Account(
                        AccountId.of("accountId"),
                        Balance.of(BigDecimal.valueOf(1000.00)),
                        AccountNumber.of("123456789"),
                        OwnerName.of("John Doe"),
                        AccountType.of("Savings")
                ),
                null,
                null,
                null,
                new Account(
                        AccountId.of("accountId"),
                        Balance.of(BigDecimal.valueOf(1000.00)),
                        AccountNumber.of("123456789"),
                        OwnerName.of("John Doe"),
                        AccountType.of("Savings")
                ),
                null,
                null,
                null
        );
    }


}