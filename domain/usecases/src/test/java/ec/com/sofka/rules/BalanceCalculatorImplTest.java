package ec.com.sofka.rules;

import ec.com.sofka.gateway.dto.TransactionDTO;
import ec.com.sofka.gateway.dto.TransactionTypeDTO;
import ec.com.sofka.rules.impl.BalanceCalculatorImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BalanceCalculatorImplTest {

    private final BalanceCalculatorImpl balanceCalculator = new BalanceCalculatorImpl();

    @Test
    @DisplayName("should subtract transaction amount and fees when transaction has discount")
    public void shouldSubtractTransactionAmountAndFees_WhenTransactionHasDiscount() {
        TransactionDTO  transaction = mock(TransactionDTO.class);
        TransactionTypeDTO transactionType = mock(TransactionTypeDTO.class);

        when(transaction.getAmount()).thenReturn(BigDecimal.valueOf(100));
        when(transactionType.getValue()).thenReturn(BigDecimal.valueOf(10));
        when(transaction.getTransactionType()).thenReturn(transactionType);
        when(transactionType.getDiscount()).thenReturn(true);

        BigDecimal currentBalance = BigDecimal.valueOf(500);

        BigDecimal result = balanceCalculator.calculate(transaction, currentBalance);

        assertEquals(BigDecimal.valueOf(390), result);
    }

    @Test
    @DisplayName("should add transaction amount and fees when transaction has no discount")
    public void shouldAddTransactionAmountAndFees_WhenTransactionHasNoDiscount() {
        TransactionDTO transaction = mock(TransactionDTO.class);
        TransactionTypeDTO transactionType = mock(TransactionTypeDTO.class);

        when(transaction.getAmount()).thenReturn(BigDecimal.valueOf(100));
        when(transactionType.getValue()).thenReturn(BigDecimal.ZERO);
        when(transaction.getTransactionType()).thenReturn(transactionType);
        when(transactionType.getDiscount()).thenReturn(false);

        BigDecimal currentBalance = BigDecimal.valueOf(500);

        BigDecimal result = balanceCalculator.calculate(transaction, currentBalance);

        assertEquals(BigDecimal.valueOf(600), result);
    }
}