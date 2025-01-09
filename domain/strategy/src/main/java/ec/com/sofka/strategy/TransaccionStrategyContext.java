package ec.com.sofka.strategy;

import ec.com.sofka.account.Account;

import java.math.BigDecimal;

public class TransaccionStrategyContext {
    private final Account account;
    private final TransaccionStrategy strategy;
    private final BigDecimal amount;

    public TransaccionStrategyContext(Account account, TransaccionStrategy strategy, BigDecimal amount) {
        this.account = account;
        this.strategy = strategy;
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public TransaccionStrategy getStrategy() {
        return strategy;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
