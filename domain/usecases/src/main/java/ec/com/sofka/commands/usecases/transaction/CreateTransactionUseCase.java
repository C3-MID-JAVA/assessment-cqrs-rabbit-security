package ec.com.sofka.commands.usecases.transaction;

import ec.com.sofka.aggregates.account.AccountAggregate;
import ec.com.sofka.queries.query.transcationType.FindTransactionTypeByIdUseCase;
import ec.com.sofka.commands.usecases.account.UpdateAccountUseCase;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.busMessage.EventBusMessage;
import ec.com.sofka.gateway.dto.TransactionDTO;
import ec.com.sofka.generics.interfaces.IUseCaseExecute;
import ec.com.sofka.mapper.AccountMapper;
import ec.com.sofka.mapper.TransactionMapper;
import ec.com.sofka.mapper.TransactionTypeMapper;
import ec.com.sofka.aggregates.account.entities.transaction.values.objects.ProcessingDate;
import ec.com.sofka.commands.AccountCommand;
import ec.com.sofka.commands.TransactionCommand;
import ec.com.sofka.queries.responses.TransactionResponse;
import ec.com.sofka.rules.BalanceCalculator;
import ec.com.sofka.rules.ValidateTransaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class CreateTransactionUseCase implements IUseCaseExecute<TransactionCommand, TransactionResponse> {
    private final IEventStore repository;
    private final ValidateTransaction validateTransaction;
    private final BalanceCalculator balanceCalculator;
    private final FindTransactionTypeByIdUseCase findTransactionTypeByIdUseCase;
    private final UpdateAccountUseCase updateAccountUseCase;
    private final EventBusMessage eventBusMessage;

    public CreateTransactionUseCase(IEventStore repository, ValidateTransaction validateTransaction, BalanceCalculator balanceCalculator, FindTransactionTypeByIdUseCase findTransactionTypeByIdUseCase, UpdateAccountUseCase updateAccountUseCase, EventBusMessage eventBusMessage) {
        this.repository = repository;
        this.updateAccountUseCase = updateAccountUseCase;
        this.validateTransaction = validateTransaction;
        this.balanceCalculator = balanceCalculator;
        this.findTransactionTypeByIdUseCase = findTransactionTypeByIdUseCase;
        this.eventBusMessage = eventBusMessage;
    }

    @Override
    public Mono<TransactionResponse> execute(TransactionCommand transactionCommand) {
        return findTransactionTypeByIdUseCase.getById(transactionCommand.getTransactionTypeId())
                .map(TransactionTypeMapper::mapToDTOFromModel)
                .map(transactionType ->
                        new TransactionDTO(
                                transactionCommand.getAccountNumber(),
                                transactionCommand.getDetails(),
                                transactionCommand.getAmount(),
                                null,
                                null,
                                transactionType))
                .flatMap(transactionDTO -> validateTransaction.validateTransaction(transactionDTO, transactionCommand.getAccountNumber()))
                .flatMap(transactionDTO -> updateBalanceAndSave(transactionDTO, transactionCommand.getAccountAggregateId()));

    }

    public Mono<TransactionResponse> updateBalanceAndSave(TransactionDTO transaction, String accountAggregateId) {
        AccountAggregate accountAggregate = new AccountAggregate();

        BigDecimal newBalance = balanceCalculator.calculate(transaction, transaction.getAccount().getBalance());
        transaction.getAccount().setBalance(newBalance);
        transaction.setProcessingDate(new ProcessingDate(transaction.getProcessingDate()).getValue());

        accountAggregate.createTransaction(
                transaction.getAccountNumber(),
                transaction.getDetails(),
                transaction.getAmount(),
                transaction.getProcessingDate(),
                AccountMapper.mapToModelFromDTO(transaction.getAccount()),
                TransactionTypeMapper.mapToModelFromDTO(transaction.getTransactionType())
        );

        AccountCommand accountCommand = new AccountCommand(
                accountAggregateId,
                transaction.getAccount().getAccountNumber(),
                transaction.getAccount().getBalance(),
                transaction.getAccount().getStatus(),
                null
        );

        return updateAccountUseCase.execute(accountCommand)
                .flatMap(transactionDTO -> {
                            return Flux.fromIterable(accountAggregate.getUncommittedEvents())
                                    .flatMap(repository::save)
                                    .doOnNext(eventBusMessage::sendEvent)
                                    .then(Mono.fromCallable(() -> {
                                        accountAggregate.markEventsAsCommitted();
                                        return TransactionMapper.mapToResponseFromModel(accountAggregate.getTransaction());
                                    }));
                        });

    }

}