package ec.com.sofka.repository.account;

import ec.com.sofka.data.ErrorLogEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IErrorLogRepository extends ReactiveMongoRepository<ErrorLogEntity, String> {

}