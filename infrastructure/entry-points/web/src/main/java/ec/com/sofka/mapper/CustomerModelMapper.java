package ec.com.sofka.mapper;

import ec.com.sofka.data.CustomerRequestDTO;
import ec.com.sofka.data.CustomerResponseDTO;
import ec.com.sofka.commands.CustomerCommand;
import ec.com.sofka.queries.responses.CustomerResponse;
import ec.com.sofka.utils.enums.StatusEnum;

public class CustomerModelMapper {

    public static CustomerCommand mapToRequest(CustomerRequestDTO customer) {
        if (customer == null) {
            return null;
        }

        return new CustomerCommand(
                customer.getFirstName(),
                customer.getLastName(),
                customer.getIdentityCard(),
                StatusEnum.ACTIVE.name().compareTo(customer.getStatus()) == 0 ? StatusEnum.ACTIVE : StatusEnum.INACTIVE
        );
    }

    public static CustomerResponseDTO mapToDTO(CustomerResponse customer) {
        if (customer == null) {
            return null;
        }

        return new CustomerResponseDTO(
                customer.getFirstName(),
                customer.getLastName(),
                customer.getIdentityCard(),
                customer.getStatus().name()
        );
    }



}