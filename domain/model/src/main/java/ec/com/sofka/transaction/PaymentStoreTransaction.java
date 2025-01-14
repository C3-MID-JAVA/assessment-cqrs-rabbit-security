package ec.com.sofka.transaction;

import ec.com.sofka.card.values.objects.AccountValue;
import ec.com.sofka.transaction.values.TransactionId;
import ec.com.sofka.transaction.values.objects.*;

import java.math.BigDecimal;

public class PaymentStoreTransaction extends Transaction {
    private final MarketName marketName;

    public PaymentStoreTransaction(TransactionId transactionId, Description description, Amount amount, TransactionType transactionType, TransactionFee transactionFee, Timestamp timestamp, AccountValue accountValue, CardValue cardValue, MarketName marketName) {
        super(transactionId, description, amount, transactionType, transactionFee, timestamp, accountValue, cardValue);
        this.marketName = marketName;
    }


    @Override
    public void processTransaction() {
        setTransactionFee(TransactionFee.of(BigDecimal.valueOf(0)));
    }

    public MarketName getMarketName() {
        return marketName;
    }
}
