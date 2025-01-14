package ec.com.sofka.gateway.dto.transaction;

import ec.com.sofka.gateway.dto.account.AccountDTO;
import ec.com.sofka.gateway.dto.card.CardDTO;
import ec.com.sofka.generics.utils.Command;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDTO extends Command {
    private String id;
    private String customerId;
    private String description;
    private BigDecimal amount;
    private String transactionType;
    private BigDecimal transactionFee;
    private LocalDateTime timestamp;
    private AccountDTO account;
    private CardDTO card;

    public TransactionDTO(String aggregateId) {
        super(null);
    }

    public TransactionDTO(String id, String customerId, String description, BigDecimal amount, String transactionType, BigDecimal transactionFee, LocalDateTime timestamp, AccountDTO account, CardDTO card) {
        super(null);
        this.id = id;
        this.customerId = customerId;

        this.description = description;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionFee = transactionFee;
        this.timestamp = timestamp;
        this.account = account;
        this.card = card;
    }

    public String getId() {
        return id;
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

    public AccountDTO getAccount() {
        return account;
    }

    public CardDTO getCard() {
        return card;
    }

    public void setCard(CardDTO card) {
        this.card = card;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }

    public void setTransactionFee(BigDecimal transactionFee) {
        this.transactionFee = transactionFee;
    }

    public String getCustomerId() {
        return customerId;
    }

}
