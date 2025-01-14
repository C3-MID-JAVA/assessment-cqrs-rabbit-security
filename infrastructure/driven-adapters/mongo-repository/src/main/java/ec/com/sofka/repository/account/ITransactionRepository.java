package ec.com.sofka.repository.account;

import ec.com.sofka.data.TransactionEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ITransactionRepository extends ReactiveMongoRepository<TransactionEntity, String> {

}