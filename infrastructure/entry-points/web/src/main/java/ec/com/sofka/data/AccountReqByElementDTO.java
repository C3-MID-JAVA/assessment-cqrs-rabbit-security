package ec.com.sofka.data;

public class AccountReqByElementDTO {
    private String customerId;
    private String accountNumber;
    private String accountId;
    // Constructor
    public AccountReqByElementDTO(String customerId, String accountNumber, String accountId) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.accountId = accountId;
    }

    // Getters y setters
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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}

