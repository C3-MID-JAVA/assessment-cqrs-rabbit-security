package ec.com.sofka.aggregate.transaction;

import ec.com.sofka.aggregate.transaction.events.TransactionCreated;
import ec.com.sofka.card.values.objects.AccountValue;
import ec.com.sofka.generics.domain.DomainActionsContainer;
import ec.com.sofka.transaction.*;
import ec.com.sofka.transaction.values.TransactionId;
import ec.com.sofka.transaction.values.objects.*;

public class PaymentHandler<T extends Transaction> extends DomainActionsContainer {

    private final Payment<T> payment;
    public PaymentHandler(Payment<T> payment) {

        this.payment = payment;

        addDomainActions((TransactionCreated event) -> {
            T transaction = (T) createTransaction(event);
            transaction.processTransaction();
            payment.setTransaction(transaction);
        });

    }

    @SuppressWarnings("unchecked")
    private T createTransaction(TransactionCreated event) {
        return (T) switch (event.getTransactionType()) {
            case "ATM" ->
                new AtmTransaction(
                        TransactionId.of(event.getId()),
                        Description.of(event.getDescription()),
                        Amount.of(event.getAmount()),
                        TransactionType.of(event.getTransactionType()),
                        TransactionFee.of(event.getTransactionFee()),
                        Timestamp.of(event.getTimestamp()),
                        AccountValue.of(event.getAccountValue()),
                        CardValue.of(event.getCardValue()),

                        AtmName.of(event.getAtmName()),
                        OperationType.of(event.getOperationType())
                );
            case "BA" -> new AccountDeposit(
                    TransactionId.of(event.getId()),
                    Description.of(event.getDescription()),
                    Amount.of(event.getAmount()),
                    TransactionType.of(event.getTransactionType()),
                    TransactionFee.of(event.getTransactionFee()),
                    Timestamp.of(event.getTimestamp()),
                    AccountValue.of(event.getAccountValue()),
                    CardValue.of(event.getCardValue()),

                    AccountValue.of(event.getAccountReceiver())
            );
            case "WP" -> new PaymentWebTransaction(
                    TransactionId.of(event.getId()),
                    Description.of(event.getDescription()),
                    Amount.of(event.getAmount()),
                    TransactionType.of(event.getTransactionType()),
                    TransactionFee.of(event.getTransactionFee()),
                    Timestamp.of(event.getTimestamp()),
                    AccountValue.of(event.getAccountValue()),
                    CardValue.of(event.getCardValue()),

                    Website.of(event.getWebsite())
            );
            case "SP" -> new PaymentStoreTransaction(
                    TransactionId.of(event.getId()),
                    Description.of(event.getDescription()),
                    Amount.of(event.getAmount()),
                    TransactionType.of(event.getTransactionType()),
                    TransactionFee.of(event.getTransactionFee()),
                    Timestamp.of(event.getTimestamp()),
                    AccountValue.of(event.getAccountValue()),
                    CardValue.of(event.getCardValue()),

                    MarketName.of(event.getMarketName())
            );
            case "BD" -> new BranchDeposit(
                    TransactionId.of(event.getId()),
                    Description.of(event.getDescription()),
                    Amount.of(event.getAmount()),
                    TransactionType.of(event.getTransactionType()),
                    TransactionFee.of(event.getTransactionFee()),
                    Timestamp.of(event.getTimestamp()),
                    AccountValue.of(event.getAccountValue()),
                    CardValue.of(event.getCardValue()),

                    BranchName.of(event.getBranchName())
            );
            default -> throw new IllegalArgumentException("Invalid transaction type: " + event.getTransactionType());
        };
    }
}
