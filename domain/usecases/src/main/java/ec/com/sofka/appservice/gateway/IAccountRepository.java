package ec.com.sofka.appservice.gateway;

import ec.com.sofka.account.Account;
import ec.com.sofka.appservice.gateway.dto.AccountDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAccountRepository {
    Flux<AccountDTO> findAll();
    Mono<AccountDTO> findById(String id);
    Mono<AccountDTO> findByAccountNumber(String accountNumber);
    Mono<AccountDTO> save(AccountDTO accountDTO);
    Mono<AccountDTO> update(AccountDTO accountDTO);
    Mono<AccountDTO> delete(AccountDTO accountDTO);
}
