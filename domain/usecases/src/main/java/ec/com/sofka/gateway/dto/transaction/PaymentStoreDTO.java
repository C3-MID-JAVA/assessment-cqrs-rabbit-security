package ec.com.sofka.gateway.dto.transaction;

import ec.com.sofka.gateway.dto.account.AccountDTO;
import ec.com.sofka.gateway.dto.card.CardDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentStoreDTO extends TransactionDTO {

    private String marketName;

    public PaymentStoreDTO(String id, String customerId, String description, BigDecimal amount, String transactionType, BigDecimal transactionFee, LocalDateTime timestamp, AccountDTO account, CardDTO card, String marketName) {
        super(id, customerId, description, amount, transactionType, transactionFee, timestamp, account, card);
        this.marketName = marketName;
    }

    public String getMarketName() {
        return marketName;
    }
}
