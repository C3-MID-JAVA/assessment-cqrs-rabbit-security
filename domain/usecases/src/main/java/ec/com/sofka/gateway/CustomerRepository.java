package ec.com.sofka.gateway;

import ec.com.sofka.gateway.dto.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerRepository {

    Mono<CustomerDTO> save(CustomerDTO customer);

    Flux<CustomerDTO> findAll();

    Mono<CustomerDTO> findById(String id);

    Mono<CustomerDTO> findByIdentityCard(String identityCard);

}