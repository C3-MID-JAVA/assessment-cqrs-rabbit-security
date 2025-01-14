package ec.com.sofka;

import ec.com.sofka.data.TransactionEntity;
import ec.com.sofka.database.account.ITransactionRepository;
import ec.com.sofka.gateway.TransactionRepository;
import ec.com.sofka.gateway.dto.TransactionDTO;
import ec.com.sofka.mapper.TransactionMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class TransactionMongoAdapter implements TransactionRepository {
    private final ITransactionRepository repository;
    private final ReactiveMongoTemplate accountMongoTemplate;

    public TransactionMongoAdapter(ITransactionRepository repository, @Qualifier("accountMongoTemplate") ReactiveMongoTemplate accountMongoTemplate) {
        this.accountMongoTemplate = accountMongoTemplate;
        this.repository = repository;
    }

    @Override
    public Mono<TransactionDTO> save(TransactionDTO transactionDTO) {
        TransactionEntity entity = TransactionMapper.toEntity(transactionDTO);
        return repository.save(entity).map(TransactionMapper::toDTO);
    }
}
