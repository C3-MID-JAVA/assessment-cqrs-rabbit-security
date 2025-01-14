package ec.com.sofka.data;

import ec.com.sofka.utils.enums.StatusEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Document(collection = "accounts")
public class AccountEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    @Field(name = "accountNumber")
    private String accountNumber;

    @Field(name = "balance")
    private BigDecimal balance;

    @Field(name = "status")
    private StatusEnum status;

    @Field(name = "customer")
    private CustomerEntity customer;

    public AccountEntity() {
    }

    public AccountEntity(String accountNumber, BigDecimal balance, StatusEnum status, CustomerEntity customer) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.status = status;
        this.customer = customer;
    }

    public AccountEntity(String id, String accountNumber, BigDecimal balance, StatusEnum status, CustomerEntity customer) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.status = status;
        this.customer = customer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customerEntity) {
        this.customer = customerEntity;
    }
}