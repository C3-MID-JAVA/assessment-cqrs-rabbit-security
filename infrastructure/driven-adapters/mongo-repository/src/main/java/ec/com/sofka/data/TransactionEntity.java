package ec.com.sofka.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Document("transactions")
public class TransactionEntity {
    @Id
    private String id;

    @Field(name = "account_number")
    private String accountNumber;

    @Field(name = "details")
    private String details;

    @Field(name = "amount")
    private BigDecimal amount;

    @Field(name = "date")
    private String processingDate;

    @Field(name = "account")
    private AccountEntity account;

    @Field(name = "transaction_type")
    private TransactionTypeEntity transactionType;

    public TransactionEntity() {
    }

    public TransactionEntity(String accountNumber, String details, BigDecimal amount, String processingDate, AccountEntity accountEntity, TransactionTypeEntity transactionTypeEntity) {
        this.accountNumber = accountNumber;
        this.details = details;
        this.amount = amount;
        this.processingDate = processingDate;
        this.account = accountEntity;
        this.transactionType = transactionTypeEntity;
    }

    public TransactionEntity(String id, String accountNumber, String details, BigDecimal amount, String processingDate, AccountEntity accountEntity, TransactionTypeEntity transactionTypeEntity) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.details = details;
        this.amount = amount;
        this.processingDate = processingDate;
        this.account = accountEntity;
        this.transactionType = transactionTypeEntity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getProcessingDate() {
        return processingDate;
    }

    public void setProcessingDate(String processingDate) {
        this.processingDate = processingDate;
    }

    public AccountEntity getAccount() {
        return account;
    }

    public void setAccount(AccountEntity accountEntity) {
        this.account = accountEntity;
    }

    public TransactionTypeEntity getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionTypeEntity transactionTypeEntity) {
        this.transactionType = transactionTypeEntity;
    }

}