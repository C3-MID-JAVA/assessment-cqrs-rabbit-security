package ec.com.sofka.aggregates.auth.handlers;

import ec.com.sofka.aggregates.auth.AuthAggregate;
import ec.com.sofka.aggregates.auth.entities.user.User;
import ec.com.sofka.aggregates.auth.entities.user.values.UserId;
import ec.com.sofka.aggregates.auth.entities.user.values.objects.Email;
import ec.com.sofka.aggregates.auth.entities.user.values.objects.Password;
import ec.com.sofka.aggregates.auth.events.UserCreated;
import ec.com.sofka.aggregates.auth.events.UserUpdated;
import ec.com.sofka.generics.domain.DomainActionsContainer;

public class UserHandler extends DomainActionsContainer {

    public UserHandler(AuthAggregate authAggregate) {
        addDomainActions((UserCreated event) -> {
            User user = new User(
                    UserId.of(event.getUserId()),
                    event.getFirstName(),
                    event.getLastName(),
                    Email.of(event.getEmail()),
                    Password.of(event.getPassword()),
                    event.getRole());

            authAggregate.setUser(user);
        });

        addDomainActions((UserUpdated event) -> {
            User user = new User(
                    UserId.of(event.getUserId()),
                    event.getFirstName(),
                    event.getLastName(),
                    Email.of(event.getEmail()),
                    Password.of(event.getPassword()),
                    event.getRole());

            authAggregate.setUser(user);
        });
    }
}
