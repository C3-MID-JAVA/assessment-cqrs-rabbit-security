package ec.com.sofka.appservice.transactions.transactionprocess;

import ec.com.sofka.*;
import ec.com.sofka.account.Account;
import ec.com.sofka.appservice.commands.usecases.ProcessTransactionUseCase;
import ec.com.sofka.appservice.queries.usecases.GetAccountByAccountNumberUseCase;
import ec.com.sofka.enums.OperationType;
import ec.com.sofka.enums.TransactionType;
import ec.com.sofka.strategy.TransaccionStrategy;
import ec.com.sofka.strategy.process.CalculateFinalBalance;
import ec.com.sofka.strategy.process.GetTransactionStrategy;
import ec.com.sofka.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

class ProcessTransactionUseCaseTest {

    private GetAccountByAccountNumberUseCase getAccountByNumberUseCase;
    private GetTransactionStrategy getTransactionStrategyUseCase;
    private CalculateFinalBalance calculateFinalBalanceUseCase;
    private SaveAccountUseCase saveAccountUseCase;
    private SaveTransactionUseCase saveTransactionUseCase;

    private ProcessTransactionUseCase useCase;

    @BeforeEach
    void setUp() {
        getAccountByNumberUseCase = mock(GetAccountByAccountNumberUseCase.class);
        getTransactionStrategyUseCase = mock(GetTransactionStrategy.class);
        calculateFinalBalanceUseCase = mock(CalculateFinalBalance.class);
        saveAccountUseCase = mock(SaveAccountUseCase.class);
        saveTransactionUseCase = mock(SaveTransactionUseCase.class);

        useCase = new ProcessTransactionUseCase(
                getAccountByNumberUseCase,
                getTransactionStrategyUseCase,
                calculateFinalBalanceUseCase,
                saveAccountUseCase,
                saveTransactionUseCase
        );
    }


    @Test
    void testProcessTransactionWithInsufficientBalance() {
        // Arrange
        Transaction transaction = new Transaction(null, BigDecimal.valueOf(200), BigDecimal.ZERO, null, TransactionType.ATM_DEPOSIT, "nonexistent");
        transaction.setType(TransactionType.ATM_WITHDRAWAL);

        Account account = new Account(BigDecimal.valueOf(1000.00),"123", "John Doe");
        TransaccionStrategy strategy = mock(TransaccionStrategy.class);
        BigDecimal transactionCost = BigDecimal.valueOf(10);
        BigDecimal finalBalance = BigDecimal.valueOf(-1010);

        when(getAccountByNumberUseCase.apply(transaction.getAccountId())).thenReturn(Mono.just(account));
        when(getTransactionStrategyUseCase.apply(account, transaction.getType(), OperationType.WITHDRAWAL, transaction.getAmount()))
                .thenReturn(Mono.just(strategy));
        when(strategy.getAmount()).thenReturn(transactionCost);
        when(calculateFinalBalanceUseCase.apply(account.getBalance(),
                transaction.getAmount(),
                transactionCost,
                OperationType.WITHDRAWAL))
                .thenReturn(finalBalance);

        // Act & Assert
        StepVerifier.create(useCase.apply(transaction, OperationType.WITHDRAWAL))
                .expectErrorMatches(throwable -> throwable instanceof ConflictException &&
                        throwable.getMessage().equals("Insufficient balance for transaction."))
                .verify();

        verify(getAccountByNumberUseCase).apply(transaction.getAccountId());
        verify(getTransactionStrategyUseCase).apply(account, transaction.getType(), OperationType.WITHDRAWAL, transaction.getAmount());
        verify(calculateFinalBalanceUseCase).apply(account.getBalance(),
                transaction.getAmount(),
                transactionCost,
                OperationType.WITHDRAWAL);
        verifyNoInteractions(saveAccountUseCase);
        verifyNoInteractions(saveTransactionUseCase);
    }
}
