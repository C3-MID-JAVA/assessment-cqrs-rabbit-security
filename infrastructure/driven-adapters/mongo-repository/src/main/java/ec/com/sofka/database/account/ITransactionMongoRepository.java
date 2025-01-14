package ec.com.sofka.database.account;


import ec.com.sofka.data.transaction.TransactionEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ITransactionMongoRepository extends ReactiveMongoRepository<TransactionEntity, String> {
}
