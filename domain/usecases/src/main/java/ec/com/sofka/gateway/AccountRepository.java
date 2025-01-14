package ec.com.sofka.gateway;

import ec.com.sofka.gateway.dto.AccountDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AccountRepository {
    /*List<AccountDTO> findAll();
    AccountDTO findById(String id);
    AccountDTO findByNumber(String number);
    AccountDTO save(AccountDTO account);
    AccountDTO update(AccountDTO account);
    AccountDTO delete(AccountDTO account);*/
    Flux<AccountDTO> findAll();
    Mono<AccountDTO> findById(String id);
    Mono<AccountDTO> findByNumber(String number);
    Mono<AccountDTO> save(AccountDTO account);
    Mono<AccountDTO> update(AccountDTO account);
    Mono<AccountDTO> delete(AccountDTO account);
}
