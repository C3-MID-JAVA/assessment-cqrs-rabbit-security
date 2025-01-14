package ec.com.sofka.aggregate.account;

import ec.com.sofka.account.Account;
import ec.com.sofka.account.values.AccountId;
import ec.com.sofka.account.values.objects.AccountType;
import ec.com.sofka.account.values.objects.Balance;
import ec.com.sofka.account.values.objects.OwnerName;
import ec.com.sofka.account.values.objects.AccountNumber;
import ec.com.sofka.aggregate.account.events.AccountBalanceUpdated;
import ec.com.sofka.aggregate.account.events.AccountCreated;
import ec.com.sofka.aggregate.account.events.CardCreated;
import ec.com.sofka.card.Card;
import ec.com.sofka.card.values.CardId;
import ec.com.sofka.card.values.objects.*;
import ec.com.sofka.generics.domain.DomainActionsContainer;


public class CustomerHandler extends DomainActionsContainer {
    public CustomerHandler(Customer customer) {
        addDomainActions((AccountCreated event) -> {
            Account account = new Account(AccountId.of(event.getId()),
                    Balance.of(event.getAccountBalance()),
                    AccountNumber.of(event.getAccountNumber()),
                    OwnerName.of(event.getOwnerName()),
                    AccountType.of(event.getAccountType()));
            customer.setAccount(account);
        });

        addDomainActions((AccountBalanceUpdated event) -> {
            Account accountUpdated = new Account(
                    AccountId.of(event.getId()),
                    Balance.of(event.getAccountBalance()),
                    AccountNumber.of(event.getAccountNumber()),
                    OwnerName.of(event.getOwnerName()),
                    AccountType.of(event.getAccountType())
            );
            customer.setAccount(accountUpdated);
        });

        addDomainActions((CardCreated event) -> {
            Card card = new Card(
                    CardId.of(event.getId()),
                    CardName.of(event.getCardName()),
                    CardNumber.of(event.getCardNumber()),
                    CardType.of(event.getCardType()),
                    CardStatus.of(event.getCardStatus()),
                    CardExpiryDate.of(event.getCardExpiryDate()),
                    CardCVV.of(event.getCardCVV()),
                    CardLimit.of(event.getCardLimit()),
                    CardHolderName.of(event.getCardHolderName()),
                    AccountValue.of(event.getAccountValue())

            );
            customer.getCards().add(card);
        });
    }
}
