package ec.com.sofka.aggregates.account.entities.customer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ec.com.sofka.generics.shared.Entity;
import ec.com.sofka.aggregates.account.entities.customer.values.CustomerId;
import ec.com.sofka.aggregates.account.entities.customer.values.objects.IdentityCard;
import ec.com.sofka.utils.enums.StatusEnum;

public class Customer extends Entity<CustomerId> {

    private String firstName;

    private String lastName;

    private final IdentityCard identityCard;

    private StatusEnum status;

    @JsonCreator
    public Customer(
            @JsonProperty("id") CustomerId customerId,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("identityCard") IdentityCard identityCard,
            @JsonProperty("status") StatusEnum status
    ) {
        super(customerId);
        this.firstName = firstName;
        this.lastName = lastName;
        this.identityCard = identityCard;
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public IdentityCard getIdentityCard() {
        return identityCard;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

}