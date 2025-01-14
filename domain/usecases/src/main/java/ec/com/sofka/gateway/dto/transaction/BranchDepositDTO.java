package ec.com.sofka.gateway.dto.transaction;

import ec.com.sofka.gateway.dto.account.AccountDTO;
import ec.com.sofka.gateway.dto.card.CardDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BranchDepositDTO extends TransactionDTO {

    private String branchName;

    public BranchDepositDTO(String id, String customerId, String description, BigDecimal amount, String transactionType, BigDecimal transactionFee, LocalDateTime timestamp, AccountDTO account, CardDTO card, String branchName) {
        super(id, customerId, description, amount, transactionType, transactionFee, timestamp, account, card);
        this.branchName = branchName;
    }

    public String getBranchName() {
        return branchName;
    }
}
