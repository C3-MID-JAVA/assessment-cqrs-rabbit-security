package ec.com.sofka.aggregate.customer;

import ec.com.sofka.account.Account;
import ec.com.sofka.account.values.AccountId;
import ec.com.sofka.account.values.objects.AccountNumber;
import ec.com.sofka.account.values.objects.Balance;
import ec.com.sofka.aggregate.customer.events.AccountBalanceUpdated;
import ec.com.sofka.aggregate.customer.events.AccountCreated;
import ec.com.sofka.aggregate.customer.events.UserCreated;
import ec.com.sofka.generics.domain.DomainActionsContainer;
import ec.com.sofka.user.User;
import ec.com.sofka.user.values.UserId;
import ec.com.sofka.user.values.objects.DocumentId;
import ec.com.sofka.user.values.objects.Name;

import java.util.List;
import java.util.stream.IntStream;

//6. Create the handler for the Aggregate root that will be on the Agreggate root constructor
public class CustomerHandler extends DomainActionsContainer {
    //7. Add the actions to the handler
    public CustomerHandler(Customer customer) {
        //8. Add the actions to the handler
        addDomainActions((AccountCreated event) -> {
            Account account = new Account(
                    AccountId.of(event.getId()),
                    Balance.of(event.getBalance()),
                    AccountNumber.of(event.getAccountNumber()),
                    UserId.of(event.getUserId()));
            customer.getAccounts().add(account);
        });

        addDomainActions((AccountBalanceUpdated event) -> {
            List<Account> accounts = customer.getAccounts();

            int index = IntStream.range(0, accounts.size())
                    .filter(i -> accounts.get(i).getAccountNumber().getValue().equals(event.getAccountNumber()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Account not found for update: " + event.getId()));

            Account updatedAccount = new Account(
                    AccountId.of(event.getId()),
                    Balance.of(event.getBalance()),
                    AccountNumber.of(event.getAccountNumber()),
                    UserId.of(event.getUserId())
            );

            accounts.set(index, updatedAccount);
        });

        addDomainActions((UserCreated event) -> {
            User user = new User(UserId.of(event.getId()), Name.of(event.getName()), DocumentId.of(event.getDocumentId()));
            customer.setUser(user);
        });
    }
}
