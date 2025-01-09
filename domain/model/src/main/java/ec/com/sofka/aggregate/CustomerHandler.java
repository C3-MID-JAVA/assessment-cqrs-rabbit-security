package ec.com.sofka.aggregate;

import ec.com.sofka.account.Account;
import ec.com.sofka.account.values.AccountId;
import ec.com.sofka.account.values.objects.Balance;
import ec.com.sofka.account.values.objects.Owner;
import ec.com.sofka.account.values.objects.NumberAcc;
import ec.com.sofka.account.values.objects.Status;
import ec.com.sofka.aggregate.events.AccountCreated;
import ec.com.sofka.aggregate.events.AccountUpdated;
import ec.com.sofka.aggregate.events.TransactionCreated;
import ec.com.sofka.generics.domain.DomainActionsContainer;
import ec.com.sofka.transaction.Transaction;
import ec.com.sofka.transaction.values.TransactionId;
import ec.com.sofka.transaction.values.objects.Amount;
import ec.com.sofka.transaction.values.objects.TransactionCost;
import ec.com.sofka.transaction.values.objects.TransactionDate;

//6. Create the handler for the Aggregate root that will be on the Agreggate root constructor
public class CustomerHandler extends DomainActionsContainer {
    //7. Add the actions to the handler
    public CustomerHandler(Customer customer) {
        //8. Add the actions to the handler
        addDomainActions((AccountCreated event) -> {
            Account account = new Account(
                    AccountId.of(event.getAccountId()),
                    Balance.of(event.getAccountBalance()),
                    NumberAcc.of(event.getAccountNumber()),
                    Owner.of(event.getName()),
                    Status.of(event.getStatus())
                    );
            customer.setAccount(account);
        });

        addDomainActions((AccountUpdated event) -> {
            Account account = new Account(
                    AccountId.of(event.getAccountId()),
                    Balance.of(event.getBalance()),
                    NumberAcc.of(event.getAccountNumber()),
                    Owner.of(event.getOwner()),
                    Status.of(event.getStatus()));
            customer.setAccount(account);
        });

        addDomainActions((TransactionCreated event) -> {
            Transaction transaction = new Transaction(
                    TransactionId.of(event.getTransactionId()),
                    Amount.of(event.getAmount()),
                    TransactionCost.of(event.getTransactionCost()),
                    TransactionDate.of(event.getDate()),
                    event.getType(), // Usando el enum directamente
                    AccountId.of(event.getAccountId())
            );
            customer.getTransactions().add(transaction);
        });

    }
}
