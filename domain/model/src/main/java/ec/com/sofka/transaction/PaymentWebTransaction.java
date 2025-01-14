package ec.com.sofka.transaction;

import ec.com.sofka.card.values.objects.AccountValue;
import ec.com.sofka.transaction.values.TransactionId;
import ec.com.sofka.transaction.values.objects.*;

import java.math.BigDecimal;

public class PaymentWebTransaction extends Transaction {
    private final Website website;

    public PaymentWebTransaction(TransactionId transactionId, Description description, Amount amount, TransactionType transactionType, TransactionFee transactionFee, Timestamp timestamp, AccountValue accountValue, CardValue cardValue, Website website) {
        super(transactionId, description, amount, transactionType, transactionFee, timestamp, accountValue, cardValue);
        this.website = website;
    }


    @Override
    public void processTransaction() {
        setTransactionFee(TransactionFee.of(BigDecimal.valueOf(0)));
    }

    public Website getWebsite() {
        return website;
    }
}
