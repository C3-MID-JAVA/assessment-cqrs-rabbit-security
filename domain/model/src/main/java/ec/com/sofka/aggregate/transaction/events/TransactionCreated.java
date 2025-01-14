package ec.com.sofka.aggregate.transaction.events;

import ec.com.sofka.account.Account;
import ec.com.sofka.aggregate.account.events.EventsEnum;
import ec.com.sofka.card.Card;
import ec.com.sofka.card.values.objects.AccountValue;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.transaction.values.objects.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionCreated extends DomainEvent {
    private final String id;
    private final String description;
    private final BigDecimal amount;
    private final String transactionType;
    private final BigDecimal transactionFee;
    private final LocalDateTime timestamp;
    private final Account accountValue;
    private final Card cardValue;

    private final String atmName;
    private final String operationType;
    private final Account accountReceiver;
    private final String marketName;
    private final String  website;
    private final String branchName;



    public TransactionCreated(String id, String description, BigDecimal amount, String transactionType, BigDecimal transactionFee, LocalDateTime timestamp, Account accountValue, Card cardValue, String atmName, String operationType, Account accountReceiver, String marketName, String website, String branchName) {
        super(EventsEnum.TRANSACTION_CREATED.name());
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionFee = transactionFee;
        this.timestamp = timestamp;
        this.accountValue = accountValue;
        this.cardValue = cardValue;
        this.atmName = atmName;
        this.operationType = operationType;
        this.accountReceiver = accountReceiver;
        this.marketName = marketName;
        this.website = website;
        this.branchName = branchName;
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

    public Account getAccountValue() {
        return accountValue;
    }

    public Card getCardValue() {
        return cardValue;
    }

    public String getAtmName() {
        return atmName;
    }

    public String getOperationType() {
        return operationType;
    }

    public Account getAccountReceiver() {
        return accountReceiver;
    }

    public String getMarketName() {
        return marketName;
    }

    public String getWebsite() {
        return website;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getId() {
        return id;
    }
}
