package ec.com.sofka.gateway.dto;

import java.math.BigDecimal;

public class TransactionDTO {
    private String id;
    private String accountNumber;
    private String details;
    private BigDecimal amount;
    private String processingDate;
    private AccountDTO accountDTO;
    private TransactionTypeDTO transactionType;

    public TransactionDTO(){}

    public TransactionDTO(String id, String accountNumber, String details, BigDecimal amount, String processingDate, AccountDTO accountDTO, TransactionTypeDTO transactionType) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.details = details;
        this.amount = amount;
        this.processingDate = processingDate;
        this.accountDTO = accountDTO;
        this.transactionType = transactionType;
    }

    public TransactionDTO(String accountNumber, String details, BigDecimal amount, String processingDate, AccountDTO accountDTO, TransactionTypeDTO transactionType) {
        this.accountNumber = accountNumber;
        this.details = details;
        this.amount = amount;
        this.processingDate = processingDate;
        this.accountDTO = accountDTO;
        this.transactionType = transactionType;
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

    public void setProcessingDate(String date) {
        this.processingDate = date;
    }

    public AccountDTO getAccount() {
        return accountDTO;
    }

    public void setAccount(AccountDTO accountDTOEntity) {
        this.accountDTO = accountDTOEntity;
    }

    public TransactionTypeDTO getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionTypeDTO transactionType) {
        this.transactionType = transactionType;
    }
}