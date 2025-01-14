package ec.com.sofka.gateway.dto.transaction;

import ec.com.sofka.gateway.dto.account.AccountDTO;
import ec.com.sofka.gateway.dto.card.CardDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentWebDTO extends TransactionDTO {
    private String website;


    public PaymentWebDTO(String id, String customerId, String description, BigDecimal amount, String transactionType, BigDecimal transactionFee, LocalDateTime timestamp, AccountDTO account, CardDTO card, String website) {
        super(id, customerId, description, amount, transactionType, transactionFee, timestamp, account, card);
        this.website = website;
    }

    public String getWebsite() {
        return website;
    }
}


