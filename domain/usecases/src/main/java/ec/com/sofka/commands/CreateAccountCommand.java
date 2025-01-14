package ec.com.sofka.commands;

import ec.com.sofka.generics.utils.Command;

import java.math.BigDecimal;

public class CreateAccountCommand extends Command {
    private final BigDecimal balance;
    private final String accountNumber;
    private final String ownerName;
    private final String accountType;

    public CreateAccountCommand(final BigDecimal balance, final String accountNumber, final String ownerName, String accountType) {
        super(null);
        this.balance = balance;
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.accountType = accountType;
    }


    public BigDecimal getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getAccountType() {
        return accountType;
    }
}
