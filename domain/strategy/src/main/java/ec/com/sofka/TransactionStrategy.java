package ec.com.sofka;

import java.math.BigDecimal;

public interface TransactionStrategy {
    BigDecimal calculateFee();
    BigDecimal calculateBalance(BigDecimal balance, BigDecimal amount);
}
