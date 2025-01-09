package ec.com.sofka.adapter;

import ec.com.sofka.ConflictException;
import ec.com.sofka.account.Account;
import ec.com.sofka.appservice.gateway.IAccountRepository;
import ec.com.sofka.appservice.gateway.dto.AccountDTO;
import ec.com.sofka.data.AccountEntity;
import ec.com.sofka.database.account.IMongoAccountRepository;
import ec.com.sofka.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class AccountAdapter implements IAccountRepository {

    private final IMongoAccountRepository repository;
    private final ReactiveMongoTemplate bankMongoTemplate;

    public AccountAdapter(IMongoAccountRepository repository, @Qualifier("accountMongoTemplate") ReactiveMongoTemplate bankMongoTemplate) {
        this.repository = repository;
        this.bankMongoTemplate = bankMongoTemplate;
    }

    @Override
    public Mono<AccountDTO> findByAccountNumber(String accountNumber) {
        return repository.findByAccountNumber(accountNumber)
                .map(AccountMapper::entityToDTO
                );
    }

    @Override
    public Mono<AccountDTO> save(AccountDTO account) {
        return repository.save(AccountMapper.DtoToEntity(account)).map(AccountMapper::entityToDTO);
    }

    @Override
    public Mono<AccountDTO> update(AccountDTO accountDTO) {
        AccountEntity accountEntity = AccountMapper.DtoToEntity(accountDTO);
        return repository.findById(accountEntity.getId())
                .flatMap(found -> {
                    AccountEntity updatedEntity = new AccountEntity(
                            found.getId(),
                            accountDTO.getName(),
                            accountDTO.getAccountNumber(),
                            found.getBalance(),
                            found.getStatus()
                    );
                    return repository.save(updatedEntity);
                })
                .map(AccountMapper::entityToDTO);
    }

    @Override
    public Mono<AccountDTO> delete(AccountDTO accountDTO) {
        AccountEntity accountEntity = AccountMapper.DtoToEntity(accountDTO);

        return repository.findById(accountEntity.getId()) // Devuelve Mono<AccountEntity>
                .flatMap(found -> {
                    AccountEntity deletedEntity = new AccountEntity(
                            found.getId(),
                            found.getName(),
                            found.getAccountNumber(),
                            found.getBalance(),
                            accountDTO.getStatus()
                    );
                    return repository.save(deletedEntity);
                })
                .map(AccountMapper::entityToDTO)
                .defaultIfEmpty(null);
    }

    @Override
    public Flux<AccountDTO> findAll() {
        return repository.findAll().map(AccountMapper::entityToDTO);
    }

    @Override
    public Mono<AccountDTO> findById(String id) {
        return repository.findById(id).map(AccountMapper::entityToDTO);
    }
}
