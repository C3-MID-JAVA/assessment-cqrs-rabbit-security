package ec.com.sofka.data.transaction;

import ec.com.sofka.data.AccountEntity;
import ec.com.sofka.data.CardEntity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "transactions")
public class AccountDepositEntity extends TransactionEntity {

    @DocumentReference
    @Field(name = "account_receiver")
    private AccountEntity accountReceiver;

    public AccountDepositEntity(String id, String description, BigDecimal amount, String transactionType, BigDecimal transactionFee, LocalDateTime timestamp, AccountEntity account, CardEntity card, AccountEntity accountReceiver) {
        super(id, description, amount, transactionType, transactionFee, timestamp, account, card);
        this.accountReceiver = accountReceiver;
    }

    public AccountEntity getAccountReceiver() {
        return accountReceiver;
    }

    public void setAccountReceiver(AccountEntity accountReceiver) {
        this.accountReceiver = accountReceiver;
    }


}
