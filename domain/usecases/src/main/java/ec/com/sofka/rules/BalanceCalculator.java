package ec.com.sofka.rules;

import ec.com.sofka.gateway.dto.TransactionDTO;

import java.math.BigDecimal;

@FunctionalInterface
public interface BalanceCalculator {
    BigDecimal calculate(TransactionDTO transaction, BigDecimal currentBalance);
}