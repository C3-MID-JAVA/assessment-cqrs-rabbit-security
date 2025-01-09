package ec.com.sofka.appservice.transactions.transactionprocess;
import ec.com.sofka.account.Account;
import ec.com.sofka.enums.OperationType;
import ec.com.sofka.enums.TransactionType;
import ec.com.sofka.strategy.TransaccionStrategy;
import ec.com.sofka.strategy.process.GetTransactionStrategy;
import ec.com.sofka.strategy.process.TransaccionStrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

class GetTransactionStrategyUseCaseTest {

    private TransaccionStrategyFactory factory;
    private GetTransactionStrategy useCase;

    @BeforeEach
    void setUp() {
        factory = mock(TransaccionStrategyFactory.class);
        useCase = new GetTransactionStrategy(factory);
    }

    @Test
    void testApplyWithValidStrategy() {
        // Arrange
        Account account = new Account(BigDecimal.valueOf(1000.00),"123", "John Doe");
        TransactionType type = TransactionType.ATM_WITHDRAWAL;
        OperationType operationType = OperationType.WITHDRAWAL;
        BigDecimal amount = BigDecimal.valueOf(500);
        TransaccionStrategy mockStrategy = mock(TransaccionStrategy.class);

        when(factory.getStrategy(type, operationType)).thenReturn(mockStrategy);
        doNothing().when(mockStrategy).validate(account, amount);

        // Act & Assert
        StepVerifier.create(useCase.apply(account, type, operationType, amount))
                .expectNext(mockStrategy)
                .verifyComplete();

        verify(factory).getStrategy(type, operationType);
        verify(mockStrategy).validate(account, amount);
    }
/*
    @Test
    void testApplyWithInvalidStrategyThrowsException() {
        // Arrange
        Account account = new Account(BigDecimal.valueOf(1000.00), "123", "John Doe");
        TransactionType type = TransactionType.ATM_DEPOSIT;
        OperationType operationType = OperationType.DEPOSIT;
        BigDecimal amount = BigDecimal.valueOf(2000); // Invalid amount exceeding balance
        TransaccionStrategy mockStrategy = mock(TransaccionStrategy.class);

        when(factory.getStrategy(type, operationType)).thenReturn(mockStrategy);
        doThrow(new IllegalArgumentException("Invalid amount"))
                .when(mockStrategy).validate(account, amount);

        // Act & Assert
        StepVerifier.create(useCase.apply(account, type, operationType, amount))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("Invalid amount"))
                .verify();

        verify(factory).getStrategy(type, operationType);
        verify(mockStrategy).validate(account, amount);
    }*/
}

