package ec.com.sofka.strategy;


import ec.com.sofka.account.Account;
import ec.com.sofka.ConflictException;

import java.math.BigDecimal;

public interface TransaccionStrategy {
    void validate(Account account, BigDecimal amount) throws ConflictException;

    BigDecimal getAmount();

}
