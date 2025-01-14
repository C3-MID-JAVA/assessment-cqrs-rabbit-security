package ec.com.sofka.data.transaction;

import ec.com.sofka.data.AccountEntity;
import ec.com.sofka.data.CardEntity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "transactions")
public class BranchDepositEntity extends TransactionEntity {

    @Field(name = "branch_name")
    private String branchName;

    public BranchDepositEntity(String id, String description, BigDecimal amount, String transactionType, BigDecimal transactionFee, LocalDateTime timestamp, AccountEntity account, CardEntity card, String branchName) {
        super(id, description, amount, transactionType, transactionFee, timestamp, account, card);
        this.branchName = branchName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}