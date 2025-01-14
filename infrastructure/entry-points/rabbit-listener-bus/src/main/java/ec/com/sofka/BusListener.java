package ec.com.sofka;


import ec.com.sofka.account.Account;
import ec.com.sofka.aggregate.account.events.AccountBalanceUpdated;
import ec.com.sofka.aggregate.account.events.AccountCreated;
import ec.com.sofka.aggregate.account.events.CardCreated;
import ec.com.sofka.aggregate.transaction.events.TransactionCreated;
import ec.com.sofka.card.Card;
import ec.com.sofka.gateway.BusEventListener;
import ec.com.sofka.gateway.dto.account.AccountDTO;
import ec.com.sofka.gateway.dto.card.CardDTO;
import ec.com.sofka.gateway.dto.transaction.*;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.queries.usecases.viewusecases.AccountSavedViewUseCase;
import ec.com.sofka.queries.usecases.viewusecases.CardSavedViewUseCase;
import ec.com.sofka.queries.usecases.viewusecases.TransactionCreatedViewUseCase;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
public class BusListener implements BusEventListener {

    private final AccountSavedViewUseCase accountSavedViewUseCase;
    private final CardSavedViewUseCase cardSavedViewUseCase;
    private final TransactionCreatedViewUseCase transactionCreatedViewUseCase;

    public BusListener(AccountSavedViewUseCase accountSavedViewUseCase, CardSavedViewUseCase cardSavedViewUseCase, TransactionCreatedViewUseCase transactionCreatedViewUseCase) {
        this.accountSavedViewUseCase = accountSavedViewUseCase;
        this.cardSavedViewUseCase = cardSavedViewUseCase;
        this.transactionCreatedViewUseCase = transactionCreatedViewUseCase;
    }

    @Override
    @RabbitListener(queues = "${account.queue.name}")
    public void receiveAccountCreated(DomainEvent event) {
        AccountCreated accountEvent = (AccountCreated) event;
        accountSavedViewUseCase.accept(new AccountDTO(
                accountEvent.getId(),
                accountEvent.getAccountBalance(),
                accountEvent.getOwnerName(),
                accountEvent.getAccountNumber(),
                accountEvent.getAccountType()
        ));

    }

    @Override
    @RabbitListener(queues = "${card.queue.name}")
    public void receiveCardCreated(DomainEvent event) {
        CardCreated cardEvent = (CardCreated) event;
        cardSavedViewUseCase.accept(
                new CardDTO(
                        cardEvent.getId(),
                        null,
                        cardEvent.getCardName(),
                        cardEvent.getCardNumber(),
                        cardEvent.getCardType(),
                        cardEvent.getCardStatus(),
                        cardEvent.getCardExpiryDate(),
                        cardEvent.getCardCVV(),
                        cardEvent.getCardLimit(),
                        cardEvent.getCardHolderName(),
                        new AccountDTO(
                                cardEvent.getAccountValue().getId().getValue(),
                                cardEvent.getAccountValue().getBalance().getValue(),
                                cardEvent.getAccountValue().getOwnerName().getValue(),
                                cardEvent.getAccountValue().getAccountNumber().getValue(),
                                cardEvent.getAccountValue().getType().getValue()
                        )

                )
        );

    }

    @Override
    @RabbitListener(queues = "${account.updated.queue.name}")
    public void receiveAccountUpdated(DomainEvent event) {
        AccountBalanceUpdated accountEvent = (AccountBalanceUpdated) event;
        accountSavedViewUseCase.accept(new AccountDTO(
                accountEvent.getId(),
                accountEvent.getAccountBalance(),
                accountEvent.getOwnerName(),
                accountEvent.getAccountNumber(),
                accountEvent.getAccountType()
        ));
    }

    @Override
    @RabbitListener(queues = "${transaction.queue.name}")
    public void receiveTransactionCreated(DomainEvent event) {
        TransactionCreated transactionEvent = (TransactionCreated) event;

        transactionCreatedViewUseCase.accept(
                constructDTO(transactionEvent)
        );

    }

   public AccountDTO  toAccountDTO(Account account) {
        if(account == null) return null;
       return new AccountDTO(
               account.getId().getValue(),
               account.getBalance().getValue(),
               account.getOwnerName().getValue(),
               account.getAccountNumber().getValue(),
               account.getType().getValue()
       );
   }

    public CardDTO  toCardDTO(Card card) {
        if(card == null) return null;
        return new CardDTO(
                card.getId().getValue(),
                null,
                card.getCardName().getValue(),
                card.getCardNumber().getValue(),
                card.getCardType().getValue(),
                card.getCardStatus().getValue(),
                card.getCardExpiryDate().getValue(),
                card.getCardCVV().getValue(),
                card.getCardLimit().getValue(),
                card.getCardHolderName().getValue(),
                toAccountDTO(card.getAccountValue().getValue())

        );
    }

    public TransactionDTO constructDTO(TransactionCreated transactionCreated) {
        return switch (transactionCreated.getTransactionType()) {
            case ConstansTrType.ATM -> {
                yield new AtmTransactionDTO(
                        transactionCreated.getId(),
                        null,
                        transactionCreated.getDescription(),
                        transactionCreated.getAmount(),
                        transactionCreated.getTransactionType(),
                        transactionCreated.getTransactionFee(),
                        transactionCreated.getTimestamp(),
                        toAccountDTO(transactionCreated.getAccountValue()),
                        toCardDTO(transactionCreated.getCardValue()),
                        transactionCreated.getAtmName(),
                        transactionCreated.getOperationType());
            }
            case ConstansTrType.BETWEEN_ACCOUNT -> {
                yield new AccountDepositDTO(
                        transactionCreated.getId(),
                        null,
                        transactionCreated.getDescription(),
                        transactionCreated.getAmount(),
                        transactionCreated.getTransactionType(),
                        transactionCreated.getTransactionFee(),
                        transactionCreated.getTimestamp(),
                        toAccountDTO(transactionCreated.getAccountValue()),
                        null,
                        toAccountDTO(transactionCreated.getAccountReceiver()),
                        null);
            }
            case ConstansTrType.STORE_PURCHASE -> {
                yield new PaymentStoreDTO(
                        transactionCreated.getId(),
                        null,
                        transactionCreated.getDescription(),
                        transactionCreated.getAmount(),
                        transactionCreated.getTransactionType(),
                        transactionCreated.getTransactionFee(),
                        transactionCreated.getTimestamp(),
                        toAccountDTO(transactionCreated.getAccountValue()),
                        toCardDTO(transactionCreated.getCardValue()),
                        transactionCreated.getMarketName());
            }
            case ConstansTrType.WEB_PURCHASE -> {
                yield new PaymentWebDTO(
                        transactionCreated.getId(),
                        null,
                        transactionCreated.getDescription(),
                        transactionCreated.getAmount(),
                        transactionCreated.getTransactionType(),
                        transactionCreated.getTransactionFee(),
                        transactionCreated.getTimestamp(),
                        toAccountDTO(transactionCreated.getAccountValue()),
                        toCardDTO(transactionCreated.getCardValue()),
                        transactionCreated.getWebsite());
            }
            case ConstansTrType.BRANCH_DEPOSIT -> {
                yield new BranchDepositDTO(
                        transactionCreated.getId(),
                        null,
                        transactionCreated.getDescription(),
                        transactionCreated.getAmount(),
                        transactionCreated.getTransactionType(),
                        transactionCreated.getTransactionFee(),
                        transactionCreated.getTimestamp(),
                        toAccountDTO(transactionCreated.getAccountValue()),
                        toCardDTO(transactionCreated.getCardValue()),
                        transactionCreated.getBranchName());
            }
            default -> throw new RuntimeException("Unsupported transaction type in Mapper");
        };
    }


}
