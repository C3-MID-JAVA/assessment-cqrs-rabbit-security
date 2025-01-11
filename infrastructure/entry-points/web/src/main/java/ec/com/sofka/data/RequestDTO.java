package ec.com.sofka.data;

import java.math.BigDecimal;

public class RequestDTO {
    public String customerId;

    public String name;

    public String accountNum;

    public BigDecimal balance;

    public String status;

    public String idUser;

    public RequestDTO(String customerId, String name, String accountNum, BigDecimal balance, String status, String idUser) {
        this.customerId = customerId;
        this.name = name;
        this.accountNum = accountNum;
        this.balance = balance;
        this.status = status;
        this.idUser= idUser;
    }


    public String getName() {
        return name;
    }

    public String getAccountNum() {
        return accountNum;
    }


    public BigDecimal getBalance() {
        return balance;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getStatus() {
        return status;
    }

    public String getIdUser() {
        return idUser;
    }


}
