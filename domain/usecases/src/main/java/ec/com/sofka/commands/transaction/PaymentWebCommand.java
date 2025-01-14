package ec.com.sofka.commands.transaction;

import ec.com.sofka.commands.CreateCardCommand;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentWebCommand extends TransactionCommand {
    private String website;

    public PaymentWebCommand(String aggregateId, String id, String customerId, String description, BigDecimal amount, String transactionType, BigDecimal transactionFee, LocalDateTime timestamp, CreateCardCommand card, String website) {
        super(aggregateId, id, customerId, description, amount, transactionType, transactionFee, timestamp, card);
        this.website = website;
    }

    public String getWebsite() {
        return website;
    }
}
