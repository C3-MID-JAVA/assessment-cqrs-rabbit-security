package ec.com.sofka.data.transaction;

import ec.com.sofka.data.AccountEntity;
import ec.com.sofka.data.CardEntity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "transactions")
public class AtmTransactionEntity extends TransactionEntity {

    @Field(name = "atm_name")
    private String atmName;

    @Field(name = "operation_type")
    private String operationType;

    public AtmTransactionEntity(String id, String description, BigDecimal amount, String transactionType, BigDecimal transactionFee, LocalDateTime timestamp, AccountEntity account, CardEntity card, String atmName, String operationType) {
        super(id, description, amount, transactionType, transactionFee, timestamp, account, card);
        this.atmName = atmName;
        this.operationType = operationType;
    }

    public String getAtmName() {
        return atmName;
    }

    public void setAtmName(String atmName) {
        this.atmName = atmName;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}
