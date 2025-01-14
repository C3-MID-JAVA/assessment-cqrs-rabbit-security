package ec.com.sofka.data;



import ec.com.sofka.ConstansTrType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Schema(description = "Request body for creating an account")
public class AccountReqDTO {

    @NotNull(message = "accountNumber" + ConstansTrType.NOT_NULL_FIELD)
    @NotBlank(message = "accountNumber" + ConstansTrType.NOT_EMPTY_FIELD)
    @Schema(description = "Unique account number assigned to the account", example = "123456789")
    private String accountNumber;

    @NotNull(message = "accountBalance" + ConstansTrType.NOT_NULL_FIELD)
    @PositiveOrZero(message = "Account balance must be positive or zero")
    @Schema(description = "Initial balance of the account", example = "500.00")
    private BigDecimal accountBalance;

    @NotNull(message = "accountType" + ConstansTrType.NOT_NULL_FIELD)
    @NotBlank(message = "accountType" + ConstansTrType.NOT_EMPTY_FIELD)
    @Schema(description = "Type of the account", example = "DEBIT")
    private String accountType;

    @NotNull(message = "accountOwner" + ConstansTrType.NOT_NULL_FIELD)
    @NotBlank(message = "accountOwner" + ConstansTrType.NOT_EMPTY_FIELD)
    @Schema(description = "Full name of the account owner. This field is mandatory.", example = "John Doe")
    private String accountOwner;

    @Schema(hidden = true)
    private  String customerId;

    public AccountReqDTO() {
    }

    public AccountReqDTO(String accountNumber, BigDecimal accountBalance, String accountType, String accountOwner, String customerId) {
        this.accountNumber = accountNumber;
        this.accountBalance = accountBalance;
        this.accountType = accountType;
        this.accountOwner = accountOwner;
        this.customerId = customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(String accountOwner) {
        this.accountOwner = accountOwner;
    }

    public String getCustomerId() {
        return customerId;
    }
}
