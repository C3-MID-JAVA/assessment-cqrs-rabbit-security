package ec.com.sofka.gateway.dto.transaction;

import ec.com.sofka.gateway.dto.account.AccountDTO;
import ec.com.sofka.gateway.dto.card.CardDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountDepositDTO extends TransactionDTO {

    private AccountDTO accountReceiver;
    private String receiverCustomerId;

    public AccountDepositDTO(String id, String customerId, String description, BigDecimal amount, String transactionType, BigDecimal transactionFee, LocalDateTime timestamp, AccountDTO account, CardDTO card, AccountDTO accountReceiver, String receiverCustomerId) {
        super(id, customerId, description, amount, transactionType, transactionFee, timestamp, account, card);
        this.accountReceiver = accountReceiver;
        this.receiverCustomerId = receiverCustomerId;
    }

    public AccountDepositDTO(String aggregateId, AccountDTO accountReceiver, String receiverCustomerId) {
        super(aggregateId);
        this.accountReceiver = accountReceiver;
        this.receiverCustomerId = receiverCustomerId;
    }

    public AccountDTO getAccountReceiver() {
        return accountReceiver;
    }

    public void setAccountReceiver(AccountDTO accountReceiver) {
        this.accountReceiver = accountReceiver;
    }

    public String getReceiverCustomerId() {
        return receiverCustomerId;
    }

    public void setReceiverCustomerId(String receiverCustomerId) {
        this.receiverCustomerId = receiverCustomerId;
    }
}
