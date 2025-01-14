package ec.com.sofka.gateway;


import ec.com.sofka.gateway.dto.account.AccountDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface IAccountRepository {
    Mono<AccountDTO> findByAccountNumber(String accountNumber);
    Flux<AccountDTO> findAllAccounts();
    Mono<AccountDTO> save(AccountDTO account);
}
