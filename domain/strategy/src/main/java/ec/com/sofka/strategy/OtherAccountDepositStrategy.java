package ec.com.sofka.strategy;

import ec.com.sofka.account.Account;
import ec.com.sofka.ConflictException;

import java.math.BigDecimal;

public class OtherAccountDepositStrategy implements TransaccionStrategy {
    private static final BigDecimal CHARGE = BigDecimal.valueOf(1.50);

    @Override
    public void validate(Account account, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ConflictException("The amount for a deposit to another account must be greater than 0.");
        }
    }

    @Override
    public BigDecimal getAmount() {
        return CHARGE;
    }
}

