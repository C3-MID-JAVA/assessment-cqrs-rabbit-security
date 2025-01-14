package ec.com.sofka.gateway;

import ec.com.sofka.gateway.dto.TransactionTypeDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionTypeRepository {

    Mono<TransactionTypeDTO> save(TransactionTypeDTO transactionType);

    Flux<TransactionTypeDTO> findAll();

    Mono<TransactionTypeDTO> findById(String transactionTypeId);

    Mono<TransactionTypeDTO> findByType(String transactionType);

}