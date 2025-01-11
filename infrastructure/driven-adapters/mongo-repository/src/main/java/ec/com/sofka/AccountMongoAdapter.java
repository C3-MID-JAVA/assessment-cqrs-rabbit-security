package ec.com.sofka;
import ec.com.sofka.data.AccountEntity;
import ec.com.sofka.database.account.IMongoRepository;
import ec.com.sofka.gateway.AccountRepository;
import ec.com.sofka.gateway.dto.AccountDTO;
import ec.com.sofka.mapper.AccountMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountMongoAdapter implements AccountRepository {

    private final IMongoRepository repository;
    //private final MongoTemplate accountMongoTemplate;

    public AccountMongoAdapter(IMongoRepository repository/*, @Qualifier("accountMongoTemplate")  MongoTemplate accountMongoTemplate*/) {
        this.repository = repository;
        //this.accountMongoTemplate = accountMongoTemplate;
    }

    @Override
    public List<AccountDTO> findAll() {
        return repository.findAll().stream().map(AccountMapper::toDTO).toList();
    }

    @Override
    public AccountDTO findById(String id) {
        AccountEntity found =  repository.findById(id).orElse(null);
        return AccountMapper.toDTO(found);
    }

    @Override
    public AccountDTO findByNumber(String number) {
        AccountEntity found = repository.findByAccountNumber(number);
        return AccountMapper.toDTO(found);
    }

    @Override
    public AccountDTO save(AccountDTO account) {
        AccountEntity a = AccountMapper.toEntity(account);
        AccountEntity saved = repository.save(a);
        return AccountMapper.toDTO(saved);
    }

    @Override
    public AccountDTO update(AccountDTO account) {
        AccountEntity a = AccountMapper.toEntity(account);


        return findById(account.getId()) != null ?
                AccountMapper.toDTO(repository.save(
                    new AccountEntity(
                            account.getId(),
                            account.getName(),
                            account.getAccountNumber(),
                            account.getBalance(),
                            account.getStatus(),
                            account.getIdUser()
                        )
                    )) : null;


    }

    @Override
    public AccountDTO delete(AccountDTO account) {
        //AccountEntity a = AccountMapper.toEntity(account);

        //AccountEntity found = repository.findById(AccountMapper.toEntity(account).getId());

        return findById(account.getId()) != null ?
                AccountMapper.toDTO(repository.save(
                        new AccountEntity(
                                account.getId(),
                                account.getName(),
                                account.getAccountNumber(),
                                account.getBalance(),
                                account.getStatus(),
                                account.getIdUser()
                        )
                )) : null;
    }
}
