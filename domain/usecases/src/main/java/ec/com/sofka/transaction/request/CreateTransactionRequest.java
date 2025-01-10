package ec.com.sofka.transaction.request;

import ec.com.sofka.generics.utils.Request;
import ec.com.sofka.transaction.TransactionType;

import java.math.BigDecimal;

public class CreateTransactionRequest extends Request {
    private final BigDecimal amount;
    private final TransactionType type;
    private final String customerId;
    private final String accountNumber;

    public CreateTransactionRequest(BigDecimal amount, TransactionType type,String accountNumber, String customerId) {
        super(null);
        this.amount = amount;
        this.type = type;
        this.customerId = customerId;
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
