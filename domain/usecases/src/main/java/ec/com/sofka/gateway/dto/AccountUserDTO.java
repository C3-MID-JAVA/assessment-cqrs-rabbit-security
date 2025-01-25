package ec.com.sofka.gateway.dto;

import java.math.BigDecimal;

public class AccountUserDTO {
    private final String id;
    private final String accountNumber;
    private final BigDecimal balance;
    private final String userId;
    private final UserDTO user;

    public AccountUserDTO(String id, String accountNumber, BigDecimal balance, String userId, UserDTO user) {
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

    public UserDTO getUser() {
        return user;
    }
}
