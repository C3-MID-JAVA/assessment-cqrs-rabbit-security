package ec.com.sofka.strategy.process;

import ec.com.sofka.enums.OperationType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
public class CalculateFinalBalance {

    public BigDecimal apply(BigDecimal saldoActual, BigDecimal monto, BigDecimal costo, OperationType tipoOperacion) {
        return tipoOperacion == OperationType.DEPOSIT
                ? saldoActual.add(monto).subtract(costo)
                : saldoActual.subtract(monto.add(costo));
    }
}