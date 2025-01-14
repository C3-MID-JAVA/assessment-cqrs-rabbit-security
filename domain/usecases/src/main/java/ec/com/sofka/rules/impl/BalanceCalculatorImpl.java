package ec.com.sofka.rules.impl;

import ec.com.sofka.gateway.dto.TransactionDTO;
import ec.com.sofka.rules.BalanceCalculator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BalanceCalculatorImpl implements BalanceCalculator {

    @Override
    public BigDecimal calculate(TransactionDTO transaction, BigDecimal currentBalance) {
        return transaction.getTransactionType().getDiscount() ?
                currentBalance.subtract(transaction.getAmount().add(transaction.getTransactionType().getValue())) :
                currentBalance.add(transaction.getAmount().add(transaction.getTransactionType().getValue()));
    }

}