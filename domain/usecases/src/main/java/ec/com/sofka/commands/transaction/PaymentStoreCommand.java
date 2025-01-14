package ec.com.sofka.commands.transaction;

import ec.com.sofka.commands.CreateCardCommand;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentStoreCommand extends  TransactionCommand {
    private final String marketName;

    public PaymentStoreCommand(String aggregateId, String id, String customerId, String description, BigDecimal amount, String transactionType, BigDecimal transactionFee, LocalDateTime timestamp, CreateCardCommand card, String marketName) {
        super(aggregateId, id, customerId, description, amount, transactionType, transactionFee, timestamp, card);
        this.marketName = marketName;
    }

    public String getMarketName() {
        return marketName;
    }
}
