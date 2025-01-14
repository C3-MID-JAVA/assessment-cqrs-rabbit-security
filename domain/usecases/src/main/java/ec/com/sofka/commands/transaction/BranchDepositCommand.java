package ec.com.sofka.commands.transaction;

import ec.com.sofka.commands.CreateCardCommand;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BranchDepositCommand extends TransactionCommand {
    private String branchName;

    public BranchDepositCommand(String aggregateId, String id, String customerId, String description, BigDecimal amount, String transactionType, BigDecimal transactionFee, LocalDateTime timestamp, CreateCardCommand card, String branchName) {
        super(aggregateId, id, customerId, description, amount, transactionType, transactionFee, timestamp, card);
        this.branchName = branchName;
    }

    public String getBranchName() {
        return branchName;
    }
}
