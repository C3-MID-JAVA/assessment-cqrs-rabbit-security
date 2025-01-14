package ec.com.sofka.data;

import ec.com.sofka.utils.enums.StatusEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("customers")
public class CustomerEntity {
    @Id
    private String id;

    @Field(name = "first_name")
    private String firstName;

    @Field(name = "last_name")
    private String lastName;

    @Indexed(unique = true)
    @Field(name = "identityCard")
    private String identityCard;

    @Field(name = "status")
    private StatusEnum status;

    public CustomerEntity() {
    }

    public CustomerEntity(String firstName, String lastName, String identityCard, StatusEnum status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.identityCard = identityCard;
        this.status = status;
    }

    public CustomerEntity(String id, String firstName, String lastName, String identityCard, StatusEnum status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.identityCard = identityCard;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

}