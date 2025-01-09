package ec.com.sofka.appservice.transactions.transactionprocess;
import ec.com.sofka.strategy.process.CalculateFinalBalance;
import ec.com.sofka.enums.OperationType;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
class CalculateFinalBalanceUseCaseTest {

    private final CalculateFinalBalance useCase = new CalculateFinalBalance();

    @Test
    void testApplyForDeposit() {
        // Arrange
        BigDecimal saldoActual = new BigDecimal("1000.00");
        BigDecimal monto = new BigDecimal("500.00");
        BigDecimal costo = new BigDecimal("10.00");
        OperationType tipoOperacion = OperationType.DEPOSIT;

        // Act
        BigDecimal result = useCase.apply(saldoActual, monto, costo, tipoOperacion);

        // Assert
        assertEquals(new BigDecimal("1490.00"), result, "El saldo final no es correcto para un depósito.");
    }

    @Test
    void testApplyForWithdrawal() {
        // Arrange
        BigDecimal saldoActual = new BigDecimal("1000.00");
        BigDecimal monto = new BigDecimal("200.00");
        BigDecimal costo = new BigDecimal("5.00");
        OperationType tipoOperacion = OperationType.WITHDRAWAL;

        // Act
        BigDecimal result = useCase.apply(saldoActual, monto, costo, tipoOperacion);

        // Assert
        assertEquals(new BigDecimal("795.00"), result, "El saldo final no es correcto para un retiro.");
    }

    @Test
    void testApplyWithZeroValues() {
        // Arrange
        BigDecimal saldoActual = BigDecimal.ZERO;
        BigDecimal monto = BigDecimal.ZERO;
        BigDecimal costo = BigDecimal.ZERO;
        OperationType tipoOperacion = OperationType.DEPOSIT;

        BigDecimal result = useCase.apply(saldoActual, monto, costo, tipoOperacion);

        assertEquals(BigDecimal.ZERO, result, "El saldo final no es correcto para valores cero.");
    }

    @Test
    void testApplyWithNegativeBalance() {
        BigDecimal saldoActual = new BigDecimal("-500.00");
        BigDecimal monto = new BigDecimal("200.00");
        BigDecimal costo = new BigDecimal("10.00");
        OperationType tipoOperacion = OperationType.WITHDRAWAL;

        BigDecimal result = useCase.apply(saldoActual, monto, costo, tipoOperacion);

        assertEquals(new BigDecimal("-710.00"), result, "El saldo final no es correcto para un saldo negativo.");
    }
}
