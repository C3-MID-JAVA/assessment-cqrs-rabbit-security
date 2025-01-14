package ec.com.sofka.queries.responses;

import java.io.Serializable;
import java.math.BigDecimal;

public class TransactionResponse implements Serializable {
    private String accountNumber;
    private String details;
    private BigDecimal amount;
    private String processingDate;
    private AccountResponse account;
    private TransactionTypeResponse transactionType;

    public TransactionResponse(String accountNumber, String details, BigDecimal amount, String processingDate, AccountResponse account, TransactionTypeResponse transactionType) {
        this.accountNumber = accountNumber;
        this.details = details;
        this.amount = amount;
        this.processingDate = processingDate;
        this.account = account;
        this.transactionType = transactionType;
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

    public TransactionTypeResponse getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionTypeResponse transactionType) {
        this.transactionType = transactionType;
    }

    public AccountResponse getAccount() {
        return account;
    }

    public void setAccount(AccountResponse account) {
        this.account = account;
    }

    public String getProcessingDate() {
        return processingDate;
    }

    public void setProcessingDate(String processingDate) {
        this.processingDate = processingDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}