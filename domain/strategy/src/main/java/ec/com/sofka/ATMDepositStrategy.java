package ec.com.sofka;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("aTMDepositStrategy")
public class ATMDepositStrategy implements TransactionStrategy {
    @Override
    public BigDecimal calculateFee() {
        return BigDecimal.valueOf(2);
    }

    @Override
    public BigDecimal calculateBalance(BigDecimal balance, BigDecimal amount) {
        BigDecimal netAmount = amount.subtract(calculateFee());
        return balance.add(netAmount);
    }
}
