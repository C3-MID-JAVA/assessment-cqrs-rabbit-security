package ec.com.sofka.database.account;

import ec.com.sofka.data.AccountEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IMongoRepository extends MongoRepository<AccountEntity, String> {
    AccountEntity findByAccountNumber(String number);
   // Optional<AccountEntity> findById(String accountId);

}
