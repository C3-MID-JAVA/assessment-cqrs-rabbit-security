package ec.com.sofka;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransferDepositStrategy implements TransactionStrategy{
    @Override
    public BigDecimal calculateFee() {
        return BigDecimal.valueOf(1.5);
    }

    @Override
    public BigDecimal calculateBalance(BigDecimal balance, BigDecimal amount) {
        BigDecimal fee = calculateFee();
        BigDecimal totalCost = amount.add(fee);
        return balance.subtract(totalCost);
    }
}
