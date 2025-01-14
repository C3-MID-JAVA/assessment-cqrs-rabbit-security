package ec.com.sofka.data;

import ec.com.sofka.ConstansTrType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;

@Schema(description = "Request body for get cards by account number")
public class AccountSimpleRequestDTO {

    @NotNull(message = "accountNumber" + ConstansTrType.NOT_NULL_FIELD)
    @Schema(description = "Unique account number assigned to the account", example = "123456789")
    private final String accountNumber;

    public AccountSimpleRequestDTO(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
