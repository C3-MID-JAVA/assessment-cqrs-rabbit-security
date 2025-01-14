package ec.com.sofka.commands.usecases;

import ec.com.sofka.ConstansTrType;
import ec.com.sofka.account.Account;
import ec.com.sofka.aggregate.account.Customer;
import ec.com.sofka.aggregate.transaction.Payment;
import ec.com.sofka.aggregate.transaction.events.TransactionCreated;
import ec.com.sofka.card.Card;
import ec.com.sofka.commands.transaction.*;
import ec.com.sofka.data.TransactionUtils;
import ec.com.sofka.gateway.*;
import ec.com.sofka.gateway.dto.account.AccountDTO;
import ec.com.sofka.gateway.dto.transaction.*;
import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.generics.interfaces.IUseCaseExecute;
import ec.com.sofka.transaction.*;
import ec.com.sofka.transaction.values.TransactionId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public class CreateTransactionUseCase implements IUseCaseExecute<TransactionCommand, TransactionDTO> {

    private final IEventStore eventRepository;
    private final BusEvent busEvent;

    public CreateTransactionUseCase(IEventStore eventRepository, BusEvent busEvent) {
        this.eventRepository = eventRepository;
        this.busEvent = busEvent;
    }


    @Override
    public Mono<TransactionDTO> execute(TransactionCommand transactionCmd) {

        if (transactionCmd.getTransactionType().equals("BA")) {
            return processAccountDepositTransaction(transactionCmd);
        }
        return processTransaction(transactionCmd);

    }


    private Mono<TransactionDTO> processAccountDepositTransaction(TransactionCommand trCmd) {
        Payment<AccountDeposit> payment = new Payment<>();
        AccountDepositCommand accountDepositDTO = (AccountDepositCommand) trCmd;

        Flux<DomainEvent> eventsSender = eventRepository.findAggregate(trCmd.getCustomerId());
        Flux<DomainEvent> eventsReceiver = eventRepository.findAggregate(accountDepositDTO.getReceiverCustomerId());

        return Customer.from(trCmd.getCustomerId(), eventsSender)
                .flatMap(sender -> {
                    Account senderAccount = sender.getAccount();

                    if (senderAccount != null) {
                        return Customer.from(accountDepositDTO.getReceiverCustomerId(), eventsReceiver)
                                .flatMap(receiver -> {
                                    Account receiverAccount = receiver.getAccount();
                                    if (receiverAccount != null) {
                                        TransactionCreated transactionCreated = new TransactionCreated(
                                                new TransactionId().getValue(),
                                                trCmd.getDescription(),
                                                trCmd.getAmount(),
                                                trCmd.getTransactionType(),
                                                trCmd.getTransactionFee(),
                                                LocalDateTime.now(),
                                                senderAccount,
                                                null,
                                                null,
                                                null,
                                                receiverAccount,
                                                null,
                                                null,
                                                null
                                        );

                                        payment.createPayment(transactionCreated);


                                        TransactionDTO transactionDTO = new TransactionDTO(
                                                null,
                                                payment.getId().getValue(),
                                                payment.getTransaction().getDescription().getValue(),
                                                payment.getTransaction().getAmount().getValue(),
                                                payment.getTransaction().getTransactionType().getValue(),
                                                payment.getTransaction().getTransactionFee().getValue(),
                                                payment.getTransaction().getTimestamp().getValue(),
                                                null,
                                                null
                                        );



                                        Account senderAc = payment.getTransaction().getAccountValue().getValue();
                                        Account receiverAc = payment.getTransaction().getAccountReceiver().getValue();


                                        sender.updateAccountBalance(
                                                senderAc.getId().getValue(),
                                                senderAc.getAccountNumber().getValue(),
                                                senderAc.getBalance().getValue()
                                                        .subtract(payment.getTransaction().getAmount().getValue())
                                                        .subtract(payment.getTransaction().getTransactionFee().getValue()),
                                                senderAc.getOwnerName().getValue(),
                                                senderAc.getType().getValue()
                                        );
                                        receiver.updateAccountBalance(
                                                receiverAc.getId().getValue(),
                                                receiverAc.getAccountNumber().getValue(),
                                                receiverAc.getBalance().getValue()
                                                        .add(payment.getTransaction().getAmount().getValue()),
                                                receiverAc.getOwnerName().getValue(),
                                                receiverAc.getType().getValue()
                                        );

                                        payment.getUncommittedEvents()
                                                .stream()
                                                .map(eventRepository::save)
                                                .forEach(busEvent::sendEventTransactionCreated);

                                        receiver.getUncommittedEvents()
                                                .stream()
                                                .map(eventRepository::save)
                                                .forEach(busEvent::sendEventAccountUpdated);

                                        sender.getUncommittedEvents()
                                                .stream()
                                                .map(eventRepository::save)
                                                .forEach(busEvent::sendEventAccountUpdated);

                                        payment.markEventsAsCommitted();
                                        sender.markEventsAsCommitted();
                                        receiver.markEventsAsCommitted();

                                        AccountDTO receiverAccountDTO = new AccountDTO(
                                                receiver.getAccount().getBalance().getValue(),
                                                receiver.getAccount().getOwnerName().getValue(),
                                                receiver.getAccount().getAccountNumber().getValue(),
                                                receiver.getAccount().getType().getValue()
                                        );


                                        TransactionDTO transDTO = TransactionUtils.generateResponse(payment);

                                        return Mono.just(transDTO);
                                    }
                                    return Mono.error(new RuntimeException("Receiver account not found"));
                                });
                    }
                    return Mono.error(new RuntimeException("Sender account not found"));
                });
    }




    private Mono<TransactionDTO> processTransaction(TransactionCommand transactionDTO) {

        Flux<DomainEvent> events = eventRepository.findAggregate(transactionDTO.getCustomerId());

        return Customer.from(transactionDTO.getCustomerId(), events)
                .flatMap(customer -> {
                    Account account = customer.getAccount();
                    Card card = customer.getCards().stream()
                            .filter(cardRec -> cardRec.getCardNumber().getValue().equals(transactionDTO.getCard().getCardNumber()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Getting card failed"));
                    if (account != null && card != null) {
                        Payment<? extends Transaction> payment = new Payment<>();
                        TransactionCreated transactionCreated = TransactionUtils.createTransactionCreated(transactionDTO, account, card);

                        payment.createPayment(transactionCreated);


                        TransactionDTO trDTO = TransactionUtils.generateResponse(payment);

                        payment.getUncommittedEvents()
                                .stream()
                                .map(eventRepository::save)
                                .forEach(busEvent::sendEventTransactionCreated);
                        Account senderAc = payment.getTransaction().getAccountValue().getValue();
                        customer.updateAccountBalance(
                                senderAc.getId().getValue(),
                                senderAc.getAccountNumber().getValue(),
                                senderAc.getBalance().getValue()
                                        .subtract(payment.getTransaction().getAmount().getValue())
                                        .subtract(payment.getTransaction().getTransactionFee().getValue()),
                                senderAc.getOwnerName().getValue(),
                                senderAc.getType().getValue()
                        );

                        customer.getUncommittedEvents()
                                .stream()
                                .map(eventRepository::save)
                                .forEach(busEvent::sendEventAccountUpdated);

                        payment.markEventsAsCommitted();
                        customer.markEventsAsCommitted();

                        return Mono.just(trDTO);
                    }
                    return Mono.error(new RuntimeException("Account or Card not found in store"));
                });
    }

}
