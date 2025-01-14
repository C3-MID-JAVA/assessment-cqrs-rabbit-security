package ec.com.sofka.transaction.commands;

import ec.com.sofka.ATMDepositStrategy;
import ec.com.sofka.ATMWithdrawalStrategy;
import ec.com.sofka.NotFoundException;
import ec.com.sofka.TransactionStrategyFactory;
import ec.com.sofka.aggregate.customer.events.AccountCreated;
import ec.com.sofka.gateway.BusEvent;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.gateway.dto.TransactionDTO;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.transaction.TransactionType;
import ec.com.sofka.transaction.command.CreateTransactionCommand;
import ec.com.sofka.transaction.command.usecases.CreateTransactionUseCase;
import ec.com.sofka.transaction.queries.responses.TransactionResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTransactionUseCaseTest {

    @Mock
    private IEventStore repository;

    @Mock
    private TransactionStrategyFactory strategyFactory;

    @Mock
    private BusEvent busEvent;

    @Mock
    private ATMDepositStrategy atmDepositStrategy;

    @Mock
    private ATMWithdrawalStrategy atmWithdrawalStrategy;

    @InjectMocks
    private CreateTransactionUseCase useCase;

    @Test
    void shouldCreateATMDepositTransactionSuccessfully() {
        String customerId = "customer-123";
        String accountId = "account-123";
        String accountNumber = "123456789";
        BigDecimal initialBalance = BigDecimal.valueOf(500.0);
        BigDecimal depositAmount = BigDecimal.valueOf(100.0);
        BigDecimal fee = BigDecimal.valueOf(2.0);
        BigDecimal netAmount = depositAmount.add(fee);
        BigDecimal expectedBalance = initialBalance.add(depositAmount);

        AccountCreated accountCreatedEvent = new AccountCreated(
                accountId,
                accountNumber,
                initialBalance,
                "user-123"
        );
        accountCreatedEvent.setAggregateRootId(customerId);

        when(repository.findAggregate(customerId, "customer"))
                .thenReturn(Flux.just(accountCreatedEvent));
        when(repository.save(any(DomainEvent.class)))
                .thenReturn(Mono.empty());

        when(strategyFactory.getStrategy(TransactionType.ATM_DEPOSIT)).thenReturn(atmDepositStrategy);
        when(atmDepositStrategy.calculateFee()).thenReturn(fee);
        when(atmDepositStrategy.calculateBalance(initialBalance, depositAmount)).thenReturn(expectedBalance);

        TransactionDTO transactionDTO = new TransactionDTO(
                "transaction-123",
                depositAmount,
                fee,
                netAmount,
                TransactionType.ATM_DEPOSIT,
                LocalDateTime.now(),
                accountId
        );

        AccountDTO updatedAccountDTO = new AccountDTO(
                accountId,
                accountNumber,
                expectedBalance,
                "user-123"
        );

        CreateTransactionCommand request = new CreateTransactionCommand(
                depositAmount,
                TransactionType.ATM_DEPOSIT,
                accountNumber,
                customerId
        );

        Mono<TransactionResponse> responseMono = useCase.execute(request);

        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(netAmount, response.getFinalAmount());
                    assertEquals(fee, response.getFee());
                    assertEquals(TransactionType.ATM_DEPOSIT, response.getType());
                    assertEquals(expectedBalance, updatedAccountDTO.getBalance());
                })
                .verifyComplete();

        verify(repository, times(1)).findAggregate(customerId, "customer");
        verify(strategyFactory, times(1)).getStrategy(TransactionType.ATM_DEPOSIT);
        verify(atmDepositStrategy, times(1)).calculateFee();
        verify(atmDepositStrategy, times(1)).calculateBalance(initialBalance, depositAmount);
    }

    @Test
    void shouldCreateATMWithdrawTransactionSuccessfully() {
        String customerId = "customer-123";
        String accountId = "account-123";
        String accountNumber = "123456789";
        BigDecimal initialBalance = BigDecimal.valueOf(500.0);
        BigDecimal withdrawAmount = BigDecimal.valueOf(100.0);
        BigDecimal fee = BigDecimal.valueOf(2.0);
        BigDecimal netAmount = withdrawAmount.add(fee);
        BigDecimal expectedBalance = initialBalance.subtract(withdrawAmount).subtract(fee);

        AccountCreated accountCreatedEvent = new AccountCreated(
                accountId,
                accountNumber,
                initialBalance,
                "user-123"
        );
        accountCreatedEvent.setAggregateRootId(customerId);

        when(repository.findAggregate(customerId, "customer"))
                .thenReturn(Flux.just(accountCreatedEvent));
        when(repository.save(any(DomainEvent.class)))
                .thenReturn(Mono.empty());

        when(strategyFactory.getStrategy(TransactionType.ATM_WITHDRAWAL)).thenReturn(atmWithdrawalStrategy);
        when(atmWithdrawalStrategy.calculateFee()).thenReturn(fee);
        when(atmWithdrawalStrategy.calculateBalance(initialBalance, withdrawAmount)).thenReturn(expectedBalance);

        TransactionDTO transactionDTO = new TransactionDTO(
                "transaction-123",
                withdrawAmount,
                fee,
                netAmount,
                TransactionType.ATM_WITHDRAWAL,
                LocalDateTime.now(),
                accountId
        );

        AccountDTO updatedAccountDTO = new AccountDTO(
                accountId,
                accountNumber,
                expectedBalance,
                "user-123"
        );

        CreateTransactionCommand request = new CreateTransactionCommand(
                withdrawAmount,
                TransactionType.ATM_WITHDRAWAL,
                accountNumber,
                customerId
        );

        Mono<TransactionResponse> responseMono = useCase.execute(request);

        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(netAmount, response.getFinalAmount());
                    assertEquals(fee, response.getFee());
                    assertEquals(TransactionType.ATM_WITHDRAWAL, response.getType());
                    assertEquals(expectedBalance, updatedAccountDTO.getBalance());
                })
                .verifyComplete();

        verify(repository, times(1)).findAggregate(customerId, "customer");
        verify(strategyFactory, times(1)).getStrategy(TransactionType.ATM_WITHDRAWAL);
        verify(atmWithdrawalStrategy, times(1)).calculateFee();
        verify(atmWithdrawalStrategy, times(1)).calculateBalance(initialBalance, withdrawAmount);
    }

    @Test
    void shouldFailWhenInsufficientFundsForATMWithdraw() {
        String customerId = "customer-123";
        String accountId = "account-123";
        String accountNumber = "123456789";
        BigDecimal initialBalance = BigDecimal.valueOf(100.0);
        BigDecimal withdrawAmount = BigDecimal.valueOf(200.0);
        BigDecimal fee = BigDecimal.valueOf(2.0);

        AccountCreated accountCreatedEvent = new AccountCreated(
                accountId,
                accountNumber,
                initialBalance,
                "user-123"
        );
        accountCreatedEvent.setAggregateRootId(customerId);

        when(repository.findAggregate(customerId, "customer"))
                .thenReturn(Flux.just(accountCreatedEvent));
        when(strategyFactory.getStrategy(TransactionType.ATM_WITHDRAWAL)).thenReturn(atmWithdrawalStrategy);
        when(atmWithdrawalStrategy.calculateFee()).thenReturn(fee);
        when(atmWithdrawalStrategy.calculateBalance(initialBalance, withdrawAmount))
                .thenThrow(new IllegalArgumentException("Insufficient funds"));

        CreateTransactionCommand request = new CreateTransactionCommand(
                withdrawAmount,
                TransactionType.ATM_WITHDRAWAL,
                accountNumber,
                customerId
        );

        Mono<TransactionResponse> responseMono = useCase.execute(request);

        StepVerifier.create(responseMono)
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().equals("Insufficient funds"))
                .verify();

        verify(repository, times(1)).findAggregate(customerId, "customer");
        verify(strategyFactory, times(1)).getStrategy(TransactionType.ATM_WITHDRAWAL);
        verify(atmWithdrawalStrategy, times(1)).calculateFee();
        verify(atmWithdrawalStrategy, times(1)).calculateBalance(initialBalance, withdrawAmount);
    }

    @Test
    void shouldFailWhenAccountNotFound() {
        String customerId = "customer-123";
        String accountNumber = "123456789";

        when(repository.findAggregate(customerId, "customer"))
                .thenReturn(Flux.empty());

        CreateTransactionCommand request = new CreateTransactionCommand(
                BigDecimal.valueOf(100.0),
                TransactionType.ATM_WITHDRAWAL,
                accountNumber,
                customerId
        );

        Mono<TransactionResponse> responseMono = useCase.execute(request);

        StepVerifier.create(responseMono)
                .expectErrorMatches(throwable ->
                        throwable instanceof NotFoundException &&
                                throwable.getMessage().equals("Account not found"))
                .verify();

        verify(repository, times(1)).findAggregate(customerId, "customer");
    }

}