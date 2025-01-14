package ec.com.sofka;

import ec.com.sofka.repository.account.ITransactionRepository;
import ec.com.sofka.gateway.TransactionRepository;
import ec.com.sofka.gateway.dto.TransactionDTO;
import ec.com.sofka.mapper.TransactionEntityMapper;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class TransactionAdapter implements TransactionRepository {
    private final ITransactionRepository transactionRepository;
    private final ReactiveMongoTemplate accountReactiveMongoTemplate;

    public TransactionAdapter(ITransactionRepository transactionRepository, ReactiveMongoTemplate accountReactiveMongoTemplate) {
        this.transactionRepository = transactionRepository;
        this.accountReactiveMongoTemplate = accountReactiveMongoTemplate;
    }

    @Override
    public Mono<TransactionDTO> save(TransactionDTO transaction) {
        return transactionRepository.save(TransactionEntityMapper.mapToEntity(transaction))
                .map(TransactionEntityMapper::mapToDTO);
    }

    @Override
    public Mono<TransactionDTO> findById(String id) {
        return transactionRepository.findById(id)
                .map(TransactionEntityMapper::mapToDTO);
    }

    @Override
    public Flux<TransactionDTO> findAll() {
        return transactionRepository.findAll()
                .map(TransactionEntityMapper::mapToDTO);
    }

}