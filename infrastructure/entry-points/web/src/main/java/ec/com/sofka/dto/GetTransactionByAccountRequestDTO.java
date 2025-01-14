package ec.com.sofka.dto;

import jakarta.validation.constraints.NotBlank;

public class GetTransactionByAccountRequestDTO {
    @NotBlank(message = "Customer id cannot be blank")
    private String customerId;

    @NotBlank(message = "Account number cannot be blank")
    private String accountNumber;

    public GetTransactionByAccountRequestDTO() {
    }

    public GetTransactionByAccountRequestDTO(String customerId, String accountNumber) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
