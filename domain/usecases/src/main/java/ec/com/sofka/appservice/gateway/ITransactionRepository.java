package ec.com.sofka.appservice.gateway;

import ec.com.sofka.appservice.gateway.dto.TransactionDTO;
import ec.com.sofka.transaction.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITransactionRepository {
    Flux<TransactionDTO> findAll();
    Mono<TransactionDTO> save(TransactionDTO transaction);
    Mono<TransactionDTO> findById(String id);
}
