package ec.com.sofka.adapter;

import ec.com.sofka.data.AccountEntity;
import ec.com.sofka.database.bank.AccountMongoRepository;
import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.mapper.AccountMapperEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class AccountMongoAdapter implements AccountRepository {

    private final AccountMongoRepository repository;
    private final ReactiveMongoTemplate bankMongoTemplate;

    public AccountMongoAdapter(AccountMongoRepository repository, @Qualifier("bankMongoTemplate") ReactiveMongoTemplate bankMongoTemplate) {
        this.repository = repository;
        this.bankMongoTemplate = bankMongoTemplate;
    }


    @Override
    public Mono<AccountDTO> findByAccountNumber(String accountNumber) {
        return repository.findByAccountNumber(accountNumber).map(AccountMapperEntity::toDTO);
    }

    @Override
    public Mono<AccountDTO> save(AccountDTO account) {
        AccountEntity accountEntity = AccountMapperEntity.toEntity(account);
        return repository.save(accountEntity).map(AccountMapperEntity::toDTO);
    }

    @Override
    public Flux<AccountDTO> getAllByUserId(String userId) {
        return repository.findByUserId(userId).map(AccountMapperEntity::toDTO);
    }
}
