package ec.com.sofka.gateway;

import ec.com.sofka.gateway.dto.AccountDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AccountRepository {
    Mono<AccountDTO> findByAccountNumber(String accountNumber);
    Mono<AccountDTO> save(AccountDTO account);
    Flux<AccountDTO> getAllByUserId(String userId);

}
