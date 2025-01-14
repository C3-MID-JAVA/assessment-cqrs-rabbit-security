package ec.com.sofka.queries.usecases;

import ec.com.sofka.gateway.CustomerRepository;
import ec.com.sofka.gateway.dto.CustomerDTO;
import ec.com.sofka.generics.interfaces.IUseCaseAccept;

public class CustomerSavedViewUseCase implements IUseCaseAccept<CustomerDTO> {
    private final CustomerRepository customerRepository;

    public CustomerSavedViewUseCase(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void accept(CustomerDTO customer) {
        customerRepository.save(customer)
                .subscribe();
    }

}