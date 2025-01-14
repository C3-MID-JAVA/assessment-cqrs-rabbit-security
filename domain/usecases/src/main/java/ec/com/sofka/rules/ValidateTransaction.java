package ec.com.sofka.rules;

import ec.com.sofka.gateway.dto.TransactionDTO;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ValidateTransaction {
    Mono<TransactionDTO> validateTransaction(TransactionDTO transaction, String accountAggregateId);
}
