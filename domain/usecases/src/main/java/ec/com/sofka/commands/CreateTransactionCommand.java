package ec.com.sofka.commands;

import ec.com.sofka.generics.utils.Command;

import ec.com.sofka.transaction.values.TransactionEnum;

import java.math.BigDecimal;

public class CreateTransactionCommand extends Command {
    private final BigDecimal amount;
    private final String type;
    private final BigDecimal cost;
    private final String idAccount;
    private final String status;

    public CreateTransactionCommand(BigDecimal amount, String type, BigDecimal cost, String idAccount) {
        super(null);
        this.amount = amount;
        this.type = type;
        this.cost = cost;
        this.idAccount = idAccount;
        this.status = TransactionEnum.TRANSACTION_ACTIVE.name();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public String getIdAccount() {
        return idAccount;
    }

    public String getStatus() {
        return status;
    }
}