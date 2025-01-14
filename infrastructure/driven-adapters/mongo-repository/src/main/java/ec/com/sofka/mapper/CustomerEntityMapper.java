package ec.com.sofka.mapper;

import ec.com.sofka.data.CustomerEntity;
import ec.com.sofka.gateway.dto.CustomerDTO;
import org.springframework.stereotype.Component;

@Component
public class CustomerEntityMapper {

    public static CustomerEntity mapToEntity(CustomerDTO customer) {
        if (customer == null) {
            return null;
        }
        return new CustomerEntity(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getIdentityCard(),
                customer.getStatus());
    }

    public static CustomerDTO mapToDTO(CustomerEntity customerEntity) {
        if (customerEntity == null) {
            return null;
        }
        return new CustomerDTO(
                customerEntity.getFirstName(),
                customerEntity.getLastName(),
                customerEntity.getIdentityCard(),
                customerEntity.getStatus()
        );
    }

}