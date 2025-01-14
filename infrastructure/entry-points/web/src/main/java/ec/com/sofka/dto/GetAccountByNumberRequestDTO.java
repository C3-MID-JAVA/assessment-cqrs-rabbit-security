package ec.com.sofka.dto;

import jakarta.validation.constraints.NotBlank;

public class GetAccountByNumberRequestDTO {
    @NotBlank(message = "Aggregate id cannot be blank")
    private String aggregateId;

    @NotBlank(message = "Account number cannot be blank")
    private String accountNumber;

    public GetAccountByNumberRequestDTO() {
    }

    public GetAccountByNumberRequestDTO(String aggregateId, String accountNumber) {
        this.aggregateId = aggregateId;
        this.accountNumber = accountNumber;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
