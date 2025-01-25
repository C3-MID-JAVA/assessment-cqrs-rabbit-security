package ec.com.sofka.account.queries.responses;

import ec.com.sofka.user.queries.responses.UserResponse;

import java.math.BigDecimal;

public class AccountUserResponse {
    private final String id;
    private final String accountNumber;
    private final BigDecimal balance;
    private final String userId;
    private final UserResponse userResponse;

    public AccountUserResponse(String id, String accountNumber, BigDecimal balance, String userId, UserResponse userResponse) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.userId = userId;
        this.userResponse = userResponse;
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

    public UserResponse getUserResponse() {
        return userResponse;
    }
}
