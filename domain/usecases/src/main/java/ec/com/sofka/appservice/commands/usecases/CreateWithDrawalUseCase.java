package ec.com.sofka.appservice.commands.usecases;

import ec.com.sofka.appservice.commands.CreateTransactionCommand;
import ec.com.sofka.appservice.queries.responses.TransactionResponse;
import ec.com.sofka.generics.interfaces.IUseCase;
import ec.com.sofka.appservice.gateway.IBusEvent;
import ec.com.sofka.enums.OperationType;
import reactor.core.publisher.Mono;

public class CreateWithDrawalUseCase implements IUseCase<CreateTransactionCommand, TransactionResponse> {


    private final ProcessTransactionUseCase processTransactionUseCase;
    private final IBusEvent busMessage;
    public CreateWithDrawalUseCase(ProcessTransactionUseCase processTransactionUseCase, IBusEvent busMessage) {
        this.processTransactionUseCase = processTransactionUseCase;
        this.busMessage = busMessage;
    }

    @Override
    public Mono<TransactionResponse> execute(CreateTransactionCommand transaction) {
        return processTransactionUseCase.apply(transaction, OperationType.WITHDRAWAL);
    }
}
