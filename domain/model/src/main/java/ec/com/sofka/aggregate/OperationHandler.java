package ec.com.sofka.aggregate;

import ec.com.sofka.aggregate.events.TransactionCreated;
import ec.com.sofka.generics.domain.DomainActionsContainer;
import ec.com.sofka.transaction.Transaction;
import ec.com.sofka.transaction.values.TransactionId;
import ec.com.sofka.transaction.values.objects.*;

public class OperationHandler extends DomainActionsContainer {
    public OperationHandler(Operation operation) {
        addDomainActions((TransactionCreated event) -> {
            Transaction transaction = new Transaction(
                    TransactionId.of(event.getTransactionId()),
                    Amount.of(event.getAmount()),
                    Type.of(event.getType()),
                    Cost.of(event.getCost()),
                    IdAccount.of(event.getIdAccount()),
                    Status.of(event.getStatus())
            );
            operation.setTransaction(transaction);
        });
    }
}