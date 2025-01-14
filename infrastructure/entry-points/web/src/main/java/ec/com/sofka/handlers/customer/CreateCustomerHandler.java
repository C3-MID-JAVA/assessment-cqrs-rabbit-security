package ec.com.sofka.handlers.customer;

import ec.com.sofka.commands.usecases.customer.CreateCustomerUseCase;
import ec.com.sofka.data.CustomerRequestDTO;
import ec.com.sofka.data.CustomerResponseDTO;
import ec.com.sofka.mapper.CustomerModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CreateCustomerHandler {
    private final CreateCustomerUseCase createCustomerUseCase;

    public CreateCustomerHandler(CreateCustomerUseCase createCustomerUseCase) {
        this.createCustomerUseCase = createCustomerUseCase;
    }

    public Mono<CustomerResponseDTO> save(CustomerRequestDTO user) {
        return createCustomerUseCase.execute(CustomerModelMapper.mapToRequest(user))
                .map(CustomerModelMapper::mapToDTO);
    }

}