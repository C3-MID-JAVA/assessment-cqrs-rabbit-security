package ec.com.sofka.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;

@Schema(description = "Object representing the data to create a account.")
public class AccountRequestDTO implements Serializable {

    @Schema(description = "Number of account", example = "2200000000")
    @NotNull(message = "number cannot be null")
    @Pattern(regexp = "^[0-9]+$", message = "Incorrect number format")
    @Size(min = 10, max = 10, message = "Incorrect number length")
    private String accountNumber;

    @Schema(description = "Balance of account", example = "200")
    @NotNull(message = "balance cannot be null")
    private BigDecimal balance;

    @Schema(description = "Status of account", example = "ACTIVE")
    @NotNull(message = "status cannot be null")
    @Pattern(regexp = "^(ACTIVE|INACTIVE)$", message = "Incorrect status")
    private String status;

    @Schema(description = "Customer Aggregate ID")
    @NotNull(message = "customerAggregateId cannot be null")
    private String customerAggregateId;

    public AccountRequestDTO() {
    }

    public AccountRequestDTO(String accountNumber, BigDecimal balance, String status, String customerAggregateId) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.status = status;
        this.customerAggregateId = customerAggregateId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getStatus() {
        return status;
    }

    public String getCustomerAggregateId() {
        return customerAggregateId;
    }

}