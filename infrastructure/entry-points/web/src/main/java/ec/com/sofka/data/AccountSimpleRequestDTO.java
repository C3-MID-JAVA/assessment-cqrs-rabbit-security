package ec.com.sofka.data;

public class AccountSimpleRequestDTO {
    private String customerId;
    private final String accountNumber;

    public AccountSimpleRequestDTO(String customerId, String accountNumber) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
