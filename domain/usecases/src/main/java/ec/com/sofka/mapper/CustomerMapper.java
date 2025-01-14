package ec.com.sofka.mapper;

import ec.com.sofka.aggregates.account.events.CustomerCreated;
import ec.com.sofka.gateway.dto.CustomerDTO;
import ec.com.sofka.aggregates.account.entities.customer.Customer;
import ec.com.sofka.aggregates.account.entities.customer.values.CustomerId;
import ec.com.sofka.aggregates.account.entities.customer.values.objects.IdentityCard;
import ec.com.sofka.queries.responses.CustomerResponse;

public class CustomerMapper {

    public static Customer mapToModelFromDTO(CustomerDTO customer) {
        if (customer == null) {
            return null;
        }
        return new Customer(
                CustomerId.of(customer.getId()),
                customer.getFirstName(),
                customer.getLastName(),
                IdentityCard.of(customer.getIdentityCard()),
                customer.getStatus());
    }

    public static CustomerResponse mapToResponseFromModel(Customer customer) {
        if (customer == null) {
            return null;
        }

        return new CustomerResponse(
                customer.getFirstName(),
                customer.getLastName(),
                customer.getIdentityCard().getValue(),
                customer.getStatus()
        );
    }

    public static CustomerResponse mapToResponseFromDTO(CustomerDTO customer) {
        if (customer == null) {
            return null;
        }

        return new CustomerResponse(
                customer.getFirstName(),
                customer.getLastName(),
                customer.getIdentityCard(),
                customer.getStatus()
        );
    }

    public static CustomerDTO mapToDTOFromModel(Customer customer) {
        if (customer == null) {
            return null;
        }

        return new CustomerDTO(
                customer.getId().getValue(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getIdentityCard().getValue(),
                customer.getStatus()
        );
    }

    public static CustomerDTO mapToDTOFromEvent(CustomerCreated customer) {
        if (customer == null) {
            return null;
        }

        return new CustomerDTO(
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getIdentityCard(),
                customer.getStatus()
        );
    }

}