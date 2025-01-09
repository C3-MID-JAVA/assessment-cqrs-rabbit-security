package ec.com.sofka.data;

import ec.com.sofka.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Request body for creating an account")
public class TransactionRequestDTO {

    private String aggregateId;

    @NotNull(message = "The account number must not be null")
    @Size(min = 10, max = 10, message = "The account number must be exactly 10 characters")
    @Schema(description = "The account number associated with the transaction (exactly 10 digits)", example = "0123456789")
    private String accountNumber;

    @NotNull(message = "The amount must not be null")
    @Positive(message = "The amount must be breater than zero")
    @Schema(description = "The amount to be transacted", example = "500.75")
    private BigDecimal amount;

    @NotNull(message = "The transaction type must not be null")
    @Schema(description = "The type of the transaction (e.g., BRANCH_DEPOSIT, PHYSICAL_PURCHASE)", example = "BRANCH_DEPOSIT")
    private TransactionType transactionType;

    public TransactionRequestDTO(String aggregateId,String accountNumber, BigDecimal amount, TransactionType transactionType) {
        this.aggregateId = aggregateId;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    public TransactionRequestDTO(){

    }

    public String getAggregateId() {
        return aggregateId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }


    public BigDecimal getAmount() {
        return amount;
    }


    public TransactionType getTransactionType() {
        return transactionType;
    }

}
