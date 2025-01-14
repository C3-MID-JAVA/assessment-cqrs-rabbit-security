package ec.com.sofka;

import ec.com.sofka.repository.account.ITransactionTypeRepository;
import ec.com.sofka.gateway.TransactionTypeRepository;
import ec.com.sofka.gateway.dto.TransactionTypeDTO;
import ec.com.sofka.mapper.TransactionTypeEntityMapper;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class TransactionTypeAdapter implements TransactionTypeRepository {
    private final ITransactionTypeRepository transactionTypeRepository;
    private final ReactiveMongoTemplate accountReactiveMongoTemplate;

    public TransactionTypeAdapter(ITransactionTypeRepository transactionTypeRepository, ReactiveMongoTemplate accountReactiveMongoTemplate) {
        this.transactionTypeRepository = transactionTypeRepository;
        this.accountReactiveMongoTemplate = accountReactiveMongoTemplate;
    }

    @Override
    public Mono<TransactionTypeDTO> save(TransactionTypeDTO transactionType) {
        return transactionTypeRepository.save(TransactionTypeEntityMapper.mapToEntity(transactionType))
                        .map(TransactionTypeEntityMapper::mapToDTO);
    }

    @Override
    public Flux<TransactionTypeDTO> findAll() {
        return transactionTypeRepository.findAll()
                .map(TransactionTypeEntityMapper::mapToDTO);
    }

    @Override
    public Mono<TransactionTypeDTO> findById(String transactionTypeId) {
        return transactionTypeRepository.findById(transactionTypeId)
                .map(TransactionTypeEntityMapper::mapToDTO);
    }

    @Override
    public Mono<TransactionTypeDTO> findByType(String transactionType) {
        return transactionTypeRepository.findByType(transactionType)
                .map(TransactionTypeEntityMapper::mapToDTO);
    }

}