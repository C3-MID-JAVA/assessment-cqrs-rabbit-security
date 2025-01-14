package ec.com.sofka.aggregates.auth.entities.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ec.com.sofka.aggregates.auth.entities.user.values.UserId;
import ec.com.sofka.aggregates.auth.entities.user.values.objects.Email;
import ec.com.sofka.aggregates.auth.entities.user.values.objects.Password;
import ec.com.sofka.generics.shared.Entity;
import ec.com.sofka.utils.enums.RoleEnum;

public class User extends Entity<UserId> {
    private java.lang.String firstName;
    private java.lang.String lastName;
    private Email email;
    private Password password;
    private RoleEnum role;

    @JsonCreator
    public User(
            @JsonProperty("id") UserId userId,
            @JsonProperty("firstName") java.lang.String firstName,
            @JsonProperty("lastName") java.lang.String lastName,
            @JsonProperty("email") Email email,
            @JsonProperty("password") Password password,
            @JsonProperty("role") RoleEnum role
    ) {
        super(userId);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public java.lang.String getFirstName() {
        return firstName;
    }

    public void setFirstName(java.lang.String firstName) {
        this.firstName = firstName;
    }

    public java.lang.String getLastName() {
        return lastName;
    }

    public void setLastName(java.lang.String lastName) {
        this.lastName = lastName;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

}