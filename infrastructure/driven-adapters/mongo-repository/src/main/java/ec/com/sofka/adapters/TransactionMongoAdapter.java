package ec.com.sofka.adapters;


import ec.com.sofka.database.account.ITransactionMongoRepository;
import ec.com.sofka.gateway.ITransactionRepository;
import ec.com.sofka.gateway.dto.transaction.TransactionDTO;
import ec.com.sofka.mapper.TransactionMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class TransactionMongoAdapter implements ITransactionRepository {

    private final ITransactionMongoRepository transactionMongoRepository;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public TransactionMongoAdapter(ITransactionMongoRepository transactionMongoRepository,@Qualifier("accountMongoTemplate") ReactiveMongoTemplate reactiveMongoTemplate) {
        this.transactionMongoRepository = transactionMongoRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Override
    public Mono<TransactionDTO> save(TransactionDTO transaction) {
        return transactionMongoRepository.save(TransactionMapper.toTransactionEntity(transaction)).map(TransactionMapper::toTransactionDTO);

    }
}
