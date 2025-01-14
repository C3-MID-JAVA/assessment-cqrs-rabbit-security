package ec.com.sofka.strategy;

import ec.com.sofka.account.Account;
import ec.com.sofka.ConflictException;
import java.math.BigDecimal;

public class BranchDepositStrategy implements TransaccionStrategy {
    private static final BigDecimal CHARGE = BigDecimal.ZERO;

    @Override
    public void validate(Account account, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ConflictException("The amount for a branch deposit must be greater than 0.");
        }
    }

    @Override
    public BigDecimal getAmount() {
        return CHARGE;
    }
}

