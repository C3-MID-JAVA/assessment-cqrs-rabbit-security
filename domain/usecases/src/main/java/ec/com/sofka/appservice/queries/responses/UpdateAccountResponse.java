package ec.com.sofka.appservice.queries.responses;


import java.math.BigDecimal;

//Response class associated to the CreateAccountUseCase
public class UpdateAccountResponse {
    private String customerId;
    private String accountNumber;
    private String name;
    private BigDecimal balance;
    private String status;


    public UpdateAccountResponse(String customerId,String accountNumber, String name, String status) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.name = name;
        this.status = status;
    }

    public UpdateAccountResponse(String customerId,String accountNumber, String name , String status, BigDecimal balance) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = balance;
        this.status = status;

    }

    public UpdateAccountResponse(){

    }


    public String getCustomerId() {
        return customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getStatus() {
        return status;
    }
}
