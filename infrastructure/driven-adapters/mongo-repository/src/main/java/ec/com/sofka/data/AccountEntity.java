package ec.com.sofka.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Document(collection = "account")
public class AccountEntity {
    @Id
    private String id;
/*
    @Field("account_id")
    private String accountId;
*/
    @Field("account_number")
    private String accountNumber;

    @Field("owner")
    private String name;

    @Field("balance")
    private BigDecimal balance;

    @Field("status_account")
    private String status;

    private String idUser;

    public AccountEntity(){

    }
    public AccountEntity(String id,  String name, String accountNumber, BigDecimal balance,  String status) {
        this.id = id;
        this.name = name;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.status = status;
    }


    public AccountEntity(String id, String name, String accountNumber, BigDecimal balance,  String status, String idUser) {
        this.id = id;
        this.name = name;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.status = status;
        this.idUser=idUser;
    }



    public String getId() {
        return id;
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

    public String getIdUser() {
        return idUser;
    }


}

