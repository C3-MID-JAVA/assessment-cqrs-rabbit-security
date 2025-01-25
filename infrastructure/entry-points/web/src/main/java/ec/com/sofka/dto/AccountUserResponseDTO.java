package ec.com.sofka.dto;

import ec.com.sofka.user.queries.responses.UserResponse;

import java.math.BigDecimal;

public class AccountUserResponseDTO {
    private final String id;
    private final String accountNumber;
    private final BigDecimal balance;
    private final String userId;
    private final UserResponse user;

    public AccountUserResponseDTO(String id, String accountNumber, BigDecimal balance, String userId, UserResponse user) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.userId = userId;
        this.user = user;
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

    public UserResponse getUser() {
        return user;
    }
}
