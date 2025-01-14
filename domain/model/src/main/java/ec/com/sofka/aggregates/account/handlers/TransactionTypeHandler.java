package ec.com.sofka.aggregates.account.handlers;

import ec.com.sofka.aggregates.account.AccountAggregate;
import ec.com.sofka.aggregates.account.events.TransactionTypeCreated;
import ec.com.sofka.aggregates.account.values.objects.Amount;
import ec.com.sofka.generics.domain.DomainActionsContainer;
import ec.com.sofka.aggregates.account.entities.transactionType.TransactionType;
import ec.com.sofka.aggregates.account.entities.transactionType.values.TransactionTypeId;

public class TransactionTypeHandler extends DomainActionsContainer {

    public TransactionTypeHandler(AccountAggregate accountAggregate) {
        addDomainActions((TransactionTypeCreated event) -> {
            TransactionType transactionType = new TransactionType(
                    TransactionTypeId.of(event.getTransactionTypeId()),
                    event.getType(),
                    event.getDescription(),
                    Amount.of(event.getValue()),
                    event.getTransactionCost(),
                    event.getDiscount(),
                    event.getStatus());

            accountAggregate.setTransactionType(transactionType);
        });
    }

}