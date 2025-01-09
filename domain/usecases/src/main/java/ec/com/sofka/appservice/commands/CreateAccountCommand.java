package ec.com.sofka.appservice.commands;

import ec.com.sofka.account.values.AccountEnum;
import ec.com.sofka.generics.utils.Command;

import java.math.BigDecimal;

//Usage of the Request class
public class CreateAccountCommand extends Command {
    private final BigDecimal balance;
    private final String numberAcc;
    private final String customerName;
    private final String status;

    public CreateAccountCommand(final String numberAcc, final String customerName, final BigDecimal balance) {
        super(null);
        this.balance = balance;
        this.numberAcc = numberAcc;
        this.customerName = customerName;
        this.status = AccountEnum.ACCOUNT_ACTIVE.name();
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getNumber() {
        return numberAcc;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getStatus() {
        return status;
    }
}

