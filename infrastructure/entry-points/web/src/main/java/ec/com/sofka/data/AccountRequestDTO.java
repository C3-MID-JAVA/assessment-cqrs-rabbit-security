package ec.com.sofka.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
@Schema(description = "Request body for creating an account")
public class AccountRequestDTO {
    public String customerId;

    @NotNull(message = "The account number must not be null")
    @Size(min = 10, max = 10, message = "The account number must be exactly 10 characters")
    @Schema(description = "The account number (exactly 10 digits)", example = "0123456789")
    public String accountNumber;

    @NotNull(message = "The initial balance must not be null")
    @PositiveOrZero(message = "The initial balance must be grater than or equal to zero")
    @Schema(description = "The initial balance of the account", example = "500")
    public BigDecimal initialBalance;

    @NotNull(message = "The owner must not be null")
    @NotBlank(message = "The owner must not be empty")
    @Schema(description = "The name of the account owner", example = "Anderson Zambrano")
    public String owner;

    public String status;

    public AccountRequestDTO(String customerId, String accountNumber, BigDecimal initialBalance, String owner,String status) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.initialBalance = initialBalance;
        this.owner = owner;
        this.status = status;
    }

    public String getCustomerId() {
        return customerId;
    }


    public String getAccountNumber() {
        return accountNumber;
    }


    public BigDecimal getInitialBalance() {
        return initialBalance;
    }


    public String getOwner() {
        return owner;
    }

    public String getStatus() {
        return status;
    }

}
