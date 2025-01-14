package ec.com.sofka.strategy;



import ec.com.sofka.account.Account;
import ec.com.sofka.ConflictException;

import java.math.BigDecimal;

public class PhysicalPurchaseStrategy implements TransaccionStrategy {
    private static final BigDecimal CHARGE = BigDecimal.ZERO;

    @Override
    public void validate(Account account, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ConflictException("The amount for a physical purchase must be greater than 0.");
        }

        account.getBalance().ensureSufficientBalance(amount);
    }

    @Override
    public BigDecimal getAmount() {
        return CHARGE;
    }
}

