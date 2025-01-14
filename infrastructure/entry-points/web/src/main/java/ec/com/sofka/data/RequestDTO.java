package ec.com.sofka.data;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class RequestDTO {
    public String customerId;

    @NotNull(message = "El campo name no debe ser nulo")
    @Size(max = 100, message = "El campo name no debe tener más de 100 caracteres")
    public String name;

    @NotNull(message = "El campo accountNumber no debe ser nulo")
    @Size(min = 9, max = 9, message = "El campo accountNumber debe tener exactamente 10 dígitos")
    public String accountNum;

    @NotNull(message = "El campo balance no debe ser nulo")
    @DecimalMin(value = "0.0", message = "El balance debe ser mayor o igual a 0.0")
    public BigDecimal balance;

    @NotNull(message = "El campo status no debe ser nulo")
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
