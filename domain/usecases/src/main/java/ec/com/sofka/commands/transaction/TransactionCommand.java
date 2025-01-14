package ec.com.sofka.commands.transaction;

import ec.com.sofka.commands.CreateCardCommand;
import ec.com.sofka.gateway.dto.account.AccountDTO;
import ec.com.sofka.gateway.dto.card.CardDTO;
import ec.com.sofka.generics.utils.Command;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionCommand extends Command {
    private String id;
    private String customerId;
    private String description;
    private BigDecimal amount;
    private String transactionType;
    private BigDecimal transactionFee;
    private LocalDateTime timestamp;
    private CreateCardCommand card;

    public TransactionCommand(String aggregateId, String id, String customerId, String description, BigDecimal amount, String transactionType, BigDecimal transactionFee, LocalDateTime timestamp, CreateCardCommand card) {
        super(null);
        this.id = id;
        this.customerId = customerId;
        this.description = description;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionFee = transactionFee;
        this.timestamp = timestamp;
        this.card = card;
    }

    protected TransactionCommand() {
        super(null);
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public BigDecimal getTransactionFee() {
        return transactionFee;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public CreateCardCommand getCard() {
        return card;
    }
}
