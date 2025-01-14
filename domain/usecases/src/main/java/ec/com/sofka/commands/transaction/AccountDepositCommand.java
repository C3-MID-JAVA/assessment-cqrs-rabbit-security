package ec.com.sofka.commands.transaction;

import ec.com.sofka.commands.CreateCardCommand;
import ec.com.sofka.gateway.dto.account.AccountDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountDepositCommand extends TransactionCommand{
    private AccountDTO accountReceiver;
    private String receiverCustomerId;

    public AccountDepositCommand(String aggregateId, String id, String customerId, String description, BigDecimal amount, String transactionType, BigDecimal transactionFee, LocalDateTime timestamp, CreateCardCommand card, AccountDTO accountReceiver, String receiverCustomerId) {
        super(aggregateId, id, customerId, description, amount, transactionType, transactionFee, timestamp, card);
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
