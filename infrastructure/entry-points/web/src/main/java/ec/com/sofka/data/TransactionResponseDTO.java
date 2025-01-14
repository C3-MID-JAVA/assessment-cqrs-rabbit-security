package ec.com.sofka.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class TransactionResponseDTO implements Serializable {

    private String accountNumber;
    private String details;
    private BigDecimal amount;
    private String processingDate;
    private AccountResponseDTO account;
    private TransactionTypeResponseDTO typeTransaction;

    public TransactionResponseDTO(String accountNumber,String details, BigDecimal amount, String processingDate, AccountResponseDTO account, TransactionTypeResponseDTO typeTransaction) {
        this.accountNumber = accountNumber;
        this.details = details;
        this.amount = amount;
        this.processingDate = processingDate;
        this.account = account;
        this.typeTransaction = typeTransaction;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public TransactionTypeResponseDTO getTypeTransaction() {
        return typeTransaction;
    }

    public void setTypeTransaction(TransactionTypeResponseDTO typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public AccountResponseDTO getAccount() {
        return account;
    }

    public void setAccount(AccountResponseDTO account) {
        this.account = account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getProcessingDate() {
        return processingDate;
    }

    public void setProcessingDate(String processingDate) {
        this.processingDate = processingDate;
    }
}