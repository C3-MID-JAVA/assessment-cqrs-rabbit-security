package ec.com.sofka;

import ec.com.sofka.repository.account.ICustomerRepository;
import ec.com.sofka.gateway.CustomerRepository;
import ec.com.sofka.gateway.dto.CustomerDTO;
import ec.com.sofka.mapper.CustomerEntityMapper;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class CustomerAdapter implements CustomerRepository {
    private final ICustomerRepository customerRepository;
    private final ReactiveMongoTemplate accountReactiveMongoTemplate;

    public CustomerAdapter(ICustomerRepository customerRepository, ReactiveMongoTemplate accountReactiveMongoTemplate) {
        this.customerRepository = customerRepository;
        this.accountReactiveMongoTemplate = accountReactiveMongoTemplate;
    }

    @Override
    public Mono<CustomerDTO> save(CustomerDTO customer) {
        return customerRepository.save(CustomerEntityMapper.mapToEntity(customer))
                .map(CustomerEntityMapper::mapToDTO);
    }

    @Override
    public Flux<CustomerDTO> findAll() {
        return customerRepository.findAll()
                .map(CustomerEntityMapper::mapToDTO);
    }

    @Override
    public Mono<CustomerDTO> findById(String id) {
        return customerRepository.findById(id)
                .map(CustomerEntityMapper::mapToDTO);
    }

    @Override
    public Mono<CustomerDTO> findByIdentityCard(String identityCard) {
        return customerRepository.findByIdentityCard(identityCard)
                .map(CustomerEntityMapper::mapToDTO);
    }

}