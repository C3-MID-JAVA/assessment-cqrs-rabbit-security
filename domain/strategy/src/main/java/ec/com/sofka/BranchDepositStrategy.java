package ec.com.sofka;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BranchDepositStrategy implements TransactionStrategy{
    @Override
    public BigDecimal calculateFee() {
        return BigDecimal.valueOf(0);
    }

    @Override
    public BigDecimal calculateBalance(BigDecimal balance, BigDecimal amount) {
        BigDecimal netAmount = amount.subtract(calculateFee());
        return balance.add(netAmount);
    }
}
