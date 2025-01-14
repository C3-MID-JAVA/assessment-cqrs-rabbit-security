package ec.com.sofka.commands.usecases.transactionType;

import ec.com.sofka.aggregates.account.AccountAggregate;
import ec.com.sofka.exceptions.ConflictException;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.gateway.IEventStore;
import ec.com.sofka.gateway.TransactionTypeRepository;
import ec.com.sofka.gateway.busMessage.EventBusMessage;
import ec.com.sofka.generics.interfaces.IUseCaseExecute;
import ec.com.sofka.mapper.TransactionTypeMapper;
import ec.com.sofka.model.ErrorMessage;
import ec.com.sofka.commands.TransactionTypeCommand;
import ec.com.sofka.queries.responses.TransactionTypeResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CreateTransactionTypeUseCase implements IUseCaseExecute<TransactionTypeCommand, TransactionTypeResponse> {
    private final IEventStore repository;
    private final TransactionTypeRepository transactionTypeRepository;
    private final ErrorBusMessage errorBusMessage;
    private final EventBusMessage eventBusMessage;

    public CreateTransactionTypeUseCase(IEventStore repository, TransactionTypeRepository transactionTypeRepository, ErrorBusMessage errorBusMessage, EventBusMessage eventBusMessage) {
        this.repository = repository;
        this.transactionTypeRepository = transactionTypeRepository;
        this.errorBusMessage = errorBusMessage;
        this.eventBusMessage = eventBusMessage;
    }

    @Override
    public Mono<TransactionTypeResponse> execute(TransactionTypeCommand transactionTypeCommand) {
        AccountAggregate accountAggregate = new AccountAggregate();

        return transactionTypeRepository.findByType(transactionTypeCommand.getType())
                .flatMap(typeFound -> {
                    errorBusMessage.sendMsg(new ErrorMessage("Transaction type is already registered (" + transactionTypeCommand.getType() + ")",
                            "Create TransactionType"));
                    return Mono.<TransactionTypeResponse>error(new ConflictException("The transaction type is already registered."));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    accountAggregate.createTransactionType(
                            transactionTypeCommand.getType(),
                            transactionTypeCommand.getDescription(),
                            transactionTypeCommand.getValue(),
                            transactionTypeCommand.getTransactionCost(),
                            transactionTypeCommand.getDiscount(),
                            transactionTypeCommand.getStatus()
                    );

                    return Flux.fromIterable(accountAggregate.getUncommittedEvents())
                                    .flatMap(repository::save)
                                    .doOnNext(eventBusMessage::sendEvent)
                            .then(Mono.fromCallable(() -> {
                                accountAggregate.markEventsAsCommitted();
                                return TransactionTypeMapper.mapToResponseFromModel(accountAggregate.getTransactionType());
                            }));

                }));
    }

}