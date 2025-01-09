package ec.com.sofka.data;

import jakarta.validation.constraints.NotNull;

public class AccountReqByIdDTO {
    private String customerId;
    private String accountNumber;

    // Constructor
    public AccountReqByIdDTO(String customerId, String accountNumber) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
    }

    // Getters y setters
    public String getCustomerId() {
        return customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}

