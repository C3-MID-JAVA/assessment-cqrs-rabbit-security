package ec.com.sofka.strategy;

import ec.com.sofka.account.Account;
import java.math.BigDecimal;
import ec.com.sofka.ConflictException;

public class ATMDepositStrategy implements TransaccionStrategy {
    private static final BigDecimal CHARGE = BigDecimal.valueOf(2.0);

    @Override
    public void validate(Account account, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ConflictException("The amount for an ATM deposit must be greater than 0.");
        }
    }

    @Override
    public BigDecimal getAmount() {
        return CHARGE;
    }
}
