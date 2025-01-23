package ec.com.sofka.appservice.commands;

import ec.com.sofka.generics.utils.Command;

import java.math.BigDecimal;

//Usage of the Request class
public class UpdateAccountCommand extends Command {
    private final BigDecimal balance;
    private final String numberAcc;
    private final String customerName;
    private final String status;

    public UpdateAccountCommand(final BigDecimal balance, final String numberAcc, final String customerName, final String status) {
        super(null);
        this.balance = balance;
        this.numberAcc = numberAcc;
        this.customerName = customerName;
        this.status = status;
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
