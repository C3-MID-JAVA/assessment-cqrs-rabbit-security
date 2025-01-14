package ec.com.sofka.commands.transaction;

import ec.com.sofka.commands.CreateCardCommand;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AtmTransactionCommand extends TransactionCommand{
    private String atmName;
    private String operationType;

    public AtmTransactionCommand(String aggregateId, String id, String customerId, String description, BigDecimal amount, String transactionType, BigDecimal transactionFee, LocalDateTime timestamp, CreateCardCommand card, String atmName, String operationType) {
        super(aggregateId, id, customerId, description, amount, transactionType, transactionFee, timestamp, card);
        this.atmName = atmName;
        this.operationType = operationType;
    }

    public String getAtmName() {
        return atmName;
    }

    public String getOperationType() {
        return operationType;
    }
}
