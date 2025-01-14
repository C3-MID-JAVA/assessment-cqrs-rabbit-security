package ec.com.sofka.gateway.dto.transaction;

import ec.com.sofka.gateway.dto.account.AccountDTO;
import ec.com.sofka.gateway.dto.card.CardDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AtmTransactionDTO extends TransactionDTO {
    private String atmName;
    private String operationType;

    public AtmTransactionDTO(String id, String customerId , String description, BigDecimal amount, String transactionType, BigDecimal transactionFee, LocalDateTime timestamp, AccountDTO account, CardDTO card, String atmName, String operationType) {
        super(id, customerId, description, amount, transactionType, transactionFee, timestamp, account, card);
        this.atmName = atmName;
        this.operationType = operationType;
    }

    public AtmTransactionDTO(String aggregateId, String atmName, String operationType) {
        super(aggregateId);
        this.atmName = atmName;
        this.operationType = operationType;
    }

    public String getAtmName() {
        return atmName;
    }

    public String getOperationType() {
        return operationType;
    }



}
