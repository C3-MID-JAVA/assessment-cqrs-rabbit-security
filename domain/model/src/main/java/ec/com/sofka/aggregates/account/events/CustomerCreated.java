package ec.com.sofka.aggregates.account.events;

import ec.com.sofka.generics.domain.DomainEvent;
import ec.com.sofka.utils.enums.EventsDetailsEnum;
import ec.com.sofka.utils.enums.StatusEnum;

public class CustomerCreated extends DomainEvent {
    private String customerId;
    private String firstName;
    private String lastName;
    private String identityCard;
    private StatusEnum status;

    public CustomerCreated(String customerId, String firstName, String lastName, String identityCard, StatusEnum status) {
        super(EventsDetailsEnum.CUSTOMER_CREATED.getEventType());
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.identityCard = identityCard;
        this.status = status;
    }

    public CustomerCreated(){
        super(EventsDetailsEnum.CUSTOMER_CREATED.getEventType());
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public StatusEnum getStatus() {
        return status;
    }

}