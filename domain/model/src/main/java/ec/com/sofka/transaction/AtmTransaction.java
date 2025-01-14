package ec.com.sofka.transaction;

import ec.com.sofka.card.values.objects.AccountValue;
import ec.com.sofka.transaction.values.TransactionId;
import ec.com.sofka.transaction.values.objects.*;

import java.math.BigDecimal;

public class AtmTransaction extends Transaction {
    private final AtmName atmName;
    private final OperationType operationType;

    public AtmTransaction(TransactionId transactionId, Description description, Amount amount, TransactionType transactionType, TransactionFee transactionFee, Timestamp timestamp, AccountValue accountValue, CardValue cardValue, AtmName atmName, OperationType operationType) {
        super(transactionId, description, amount, transactionType, transactionFee, timestamp, accountValue, cardValue);
        this.atmName = atmName;
        this.operationType = operationType;
    }


    @Override
    public void processTransaction() {
        if(this.operationType.equals(OperationType.of("ATM_DEBIT"))) {
            setTransactionFee(TransactionFee.of(BigDecimal.valueOf(1)));;
        } else {
            setTransactionFee(TransactionFee.of(BigDecimal.valueOf(2)));
        }

    }


    public AtmName getAtmName() {
        return atmName;
    }

    public OperationType getOperationType() {
        return operationType;
    }
}
