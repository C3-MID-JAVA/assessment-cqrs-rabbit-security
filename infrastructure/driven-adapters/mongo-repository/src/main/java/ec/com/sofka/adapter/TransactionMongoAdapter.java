package ec.com.sofka.adapter;

import ec.com.sofka.data.TransactionEntity;
import ec.com.sofka.database.bank.TransactionMongoRepository;
import ec.com.sofka.gateway.TransactionRepository;
import ec.com.sofka.gateway.dto.TransactionDTO;
import ec.com.sofka.mapper.TransactionMapperEntity;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class TransactionMongoAdapter implements TransactionRepository {

    private final TransactionMongoRepository repository;
    private final ReactiveMongoTemplate bankMongoTemplate;

    public TransactionMongoAdapter(TransactionMongoRepository repository, ReactiveMongoTemplate bankMongoTemplate) {
        this.repository = repository;
        this.bankMongoTemplate = bankMongoTemplate;
    }

    @Override
    public Mono<TransactionDTO> save(TransactionDTO transaction) {
        TransactionEntity transactionEntity = TransactionMapperEntity.toEntity(transaction);
        return repository.save(transactionEntity).map(TransactionMapperEntity::fromEntity);
    }

    @Override
    public Flux<TransactionDTO> getAllByAccountId(String accountId) {
        return repository.findAllByAccountId(accountId).map(TransactionMapperEntity::fromEntity);
    }
}