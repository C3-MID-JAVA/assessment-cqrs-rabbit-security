package ec.com.sofka.gateway;

import ec.com.sofka.model.ErrorMessage;
import reactor.core.publisher.Mono;

public interface ErrorLogRepository {

    Mono<ErrorMessage> save(ErrorMessage errorMessage);

}
