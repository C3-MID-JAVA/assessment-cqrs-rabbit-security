package ec.com.sofka.account.values.objects;

import ec.com.sofka.ConflictException;
import ec.com.sofka.generics.interfaces.IValueObject;

import java.math.BigDecimal;

//4. Creation of a value object: In Value objects is where validations must go.
//Objects values garantees the integrity of the data
public class Balance implements IValueObject<BigDecimal> {
    private final BigDecimal value;

    private Balance(final BigDecimal value) {
        this.value = validate(value);
    }

    public static Balance of(final BigDecimal value) {
        return new Balance(value);
    }

    @Override
    public BigDecimal getValue() {
        return this.value;
    }

    public void ensureSufficientBalance(final BigDecimal amount) {
        if (this.value.compareTo(amount) < 0) {
            throw new ConflictException("Insufficient balance");
        }
    }

    public void ensureSufficientBalanceWithCost(final BigDecimal amount, final BigDecimal cost) {
        BigDecimal total = amount.add(cost);
        if (this.value.compareTo(total) < 0) {
            throw new ConflictException("Insufficient balance including cost");
        }
    }

    //hello validations
    private BigDecimal validate(final BigDecimal value){
        if(value == null){
            throw new ConflictException("The balance can't be null");
        }

        if(value.compareTo(BigDecimal.ZERO) < 0){
            throw new ConflictException("The balance must be greater than 0");
        }
        return value;
    }
}
