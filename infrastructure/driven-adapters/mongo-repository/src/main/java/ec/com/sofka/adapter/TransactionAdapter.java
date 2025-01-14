package ec.com.sofka.adapter;

import ec.com.sofka.appservice.gateway.dto.TransactionDTO;
import ec.com.sofka.database.account.IMongoAccountRepository;
import ec.com.sofka.mapper.AccountMapper;
import ec.com.sofka.transaction.Transaction;
import ec.com.sofka.database.account.IMongoTransactionRepository;
import ec.com.sofka.appservice.gateway.ITransactionRepository;
import ec.com.sofka.mapper.TransactionMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@Repository
public class TransactionAdapter implements ITransactionRepository {
    private final IMongoTransactionRepository repository;
    private final ReactiveMongoTemplate bankMongoTemplate;

    public TransactionAdapter(IMongoTransactionRepository repository,  @Qualifier("accountMongoTemplate") ReactiveMongoTemplate bankMongoTemplate) {
        this.repository = repository;
        this.bankMongoTemplate = bankMongoTemplate;
    }

    @Override
    public Flux<TransactionDTO> findAll() {
        return repository.findAll().map(TransactionMapper::transactionEntityToTransaction);
    }

    @Override
    public Mono<TransactionDTO> save(TransactionDTO transaction) {
        return repository.save(TransactionMapper.transactionToAccountEntity(transaction))
                .map(TransactionMapper::transactionEntityToTransaction);
    }

    @Override
    public Flux<TransactionDTO> findAllTransactionById(String accountNumber) {
        return repository.findAllByAccountId(accountNumber)
                .map(TransactionMapper::transactionEntityToTransaction);
    }

}
