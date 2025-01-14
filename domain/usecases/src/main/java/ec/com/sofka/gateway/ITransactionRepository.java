package ec.com.sofka.gateway;

import ec.com.sofka.gateway.dto.transaction.TransactionDTO;
import reactor.core.publisher.Mono;

public interface ITransactionRepository {
    Mono<TransactionDTO> save(TransactionDTO transaction);
}
