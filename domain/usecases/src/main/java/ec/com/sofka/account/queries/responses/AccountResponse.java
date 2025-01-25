package ec.com.sofka.account.queries.responses;

import java.math.BigDecimal;

public class AccountResponse {
    private final String id;
    private final String accountNumber;
    private final BigDecimal balance;
    private final String userId;
    private String customerId;

    public AccountResponse(String id, String accountNumber, BigDecimal balance, String userId) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.userId = userId;
    }

    public AccountResponse(String id, String accountNumber, BigDecimal balance, String userId, String customerId) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.userId = userId;
        this.customerId = customerId;
    }

    public String getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getUserId() {
        return userId;
    }

    public String getCustomerId() {
        return customerId;
    }
}
