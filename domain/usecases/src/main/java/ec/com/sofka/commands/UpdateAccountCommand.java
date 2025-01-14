package ec.com.sofka.commands;

import ec.com.sofka.generics.utils.Command;

import java.math.BigDecimal;

//Usage of the Request class
public class UpdateAccountCommand extends Command {
    private final BigDecimal balance;
    private final String numberAcc;
    private final String customerName;
    private final String status;
    private final String  idUser;

    public UpdateAccountCommand(final String aggregateId, final BigDecimal balance, final String numberAcc, final String customerName, final String status, final String idUser) {
        super(aggregateId);
        this.balance = balance;
        this.numberAcc = numberAcc;
        this.customerName = customerName;
        this.status = status;
        this.idUser = idUser;
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

    public String getIdUser() {
        return idUser;
    }
}
