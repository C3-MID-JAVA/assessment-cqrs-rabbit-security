package ec.com.sofka.queries.responses.account;


import java.math.BigDecimal;

public class CreateAccountResponse  {
    private final String customerId;
    private final String accountNumber;
    private final String ownerName;
    private final String accountType;
    private final BigDecimal accountBalance;

    public CreateAccountResponse(String customerId, String accountNumber, String name, String accountType, BigDecimal accountBalance) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.ownerName = name;
        this.accountType = accountType;
        this.accountBalance = accountBalance;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getAccountType() {
        return accountType;
    }
    public BigDecimal getAccountBalance() {
        return accountBalance;
    }
}
