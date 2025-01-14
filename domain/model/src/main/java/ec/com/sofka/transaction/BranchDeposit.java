package ec.com.sofka.transaction;

import ec.com.sofka.card.values.objects.AccountValue;
import ec.com.sofka.transaction.values.TransactionId;
import ec.com.sofka.transaction.values.objects.*;

import java.math.BigDecimal;

public class BranchDeposit extends Transaction {
    private final BranchName branchName;

    public BranchDeposit(TransactionId transactionId, Description description, Amount amount, TransactionType transactionType, TransactionFee transactionFee, Timestamp timestamp, AccountValue accountValue, CardValue cardValue, BranchName branchName) {
        super(transactionId, description, amount, transactionType, transactionFee, timestamp, accountValue, cardValue);
        this.branchName = branchName;
    }


    @Override
    public void processTransaction() {
        setTransactionFee(TransactionFee.of(BigDecimal.valueOf(0)));
    }

    public BranchName getBranchName() {
        return branchName;
    }
}
