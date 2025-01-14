package ec.com.sofka.transaction;

import ec.com.sofka.account.Account;
import ec.com.sofka.card.values.objects.AccountValue;
import ec.com.sofka.transaction.values.TransactionId;
import ec.com.sofka.transaction.values.objects.*;

import java.math.BigDecimal;

public class AccountDeposit extends Transaction {
    private final AccountValue accountReceiver;

    public AccountDeposit(TransactionId transactionId, Description description, Amount amount, TransactionType transactionType, TransactionFee transactionFee, Timestamp timestamp, AccountValue accountValue, CardValue cardValue, AccountValue accountReceiver) {
        super(transactionId, description, amount, transactionType, transactionFee, timestamp, accountValue, cardValue);

        this.accountReceiver = accountReceiver;
    }

    @Override
    public void processTransaction() {
        TransactionFee.of(BigDecimal.valueOf(1.5));
    }

    public AccountValue getAccountReceiver() {
        return accountReceiver;
    }

}
