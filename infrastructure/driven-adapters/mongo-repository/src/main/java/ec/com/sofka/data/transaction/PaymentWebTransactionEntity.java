package ec.com.sofka.data.transaction;

import ec.com.sofka.data.AccountEntity;
import ec.com.sofka.data.CardEntity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "transactions")
public class PaymentWebTransactionEntity extends TransactionEntity {

    @Field(name = "website")
    private String website;

    public PaymentWebTransactionEntity(String id, String description, BigDecimal amount, String transactionType, BigDecimal transactionFee, LocalDateTime timestamp, AccountEntity account, CardEntity card, String website) {
        super(id, description, amount, transactionType, transactionFee, timestamp, account, card);
        this.website = website;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
