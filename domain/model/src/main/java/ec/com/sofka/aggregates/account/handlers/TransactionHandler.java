package ec.com.sofka.aggregates.account.handlers;

import ec.com.sofka.aggregates.account.AccountAggregate;
import ec.com.sofka.aggregates.account.events.TransactionCreated;
import ec.com.sofka.aggregates.account.values.objects.Amount;
import ec.com.sofka.generics.domain.DomainActionsContainer;
import ec.com.sofka.aggregates.account.entities.transaction.Transaction;
import ec.com.sofka.aggregates.account.entities.transaction.values.TransactionId;
import ec.com.sofka.aggregates.account.entities.transaction.values.objects.ProcessingDate;
import ec.com.sofka.aggregates.account.entities.transaction.values.objects.TransactionAccount;

public class TransactionHandler extends DomainActionsContainer {

    public TransactionHandler(AccountAggregate accountAggregate) {
        addDomainActions((TransactionCreated event) -> {
            Transaction transaction = new Transaction(
                    TransactionId.of(event.getTransactionId()),
                    TransactionAccount.of(event.getAccountNumber()),
                    event.getDetails(),
                    Amount.of(event.getAmount()),
                    ProcessingDate.of(event.getProcessingDate()),
                    event.getAccount(),
                    event.getTransactionType());

            accountAggregate.setTransaction(transaction);
        });
    }

}