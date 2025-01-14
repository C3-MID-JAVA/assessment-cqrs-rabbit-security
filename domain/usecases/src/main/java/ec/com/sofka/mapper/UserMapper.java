package ec.com.sofka.mapper;

import ec.com.sofka.aggregates.auth.entities.user.User;
import ec.com.sofka.aggregates.auth.entities.user.values.UserId;
import ec.com.sofka.aggregates.auth.entities.user.values.objects.Email;
import ec.com.sofka.aggregates.auth.entities.user.values.objects.Password;
import ec.com.sofka.aggregates.auth.events.UserCreated;
import ec.com.sofka.aggregates.auth.events.UserUpdated;
import ec.com.sofka.gateway.dto.UserDTO;
import ec.com.sofka.queries.responses.UserResponse;

public class UserMapper {
    public static User mapToModelFromDTO(UserDTO user) {
        if (user == null) {
            return null;
        }

        return new User(
                UserId.of(user.getId()),
                user.getFirstName(),
                user.getLastName(),
                Email.of(user.getEmail()),
                Password.of(user.getPassword()),
                user.getRole());
    }

    public static UserResponse mapToResponseFromModel(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponse(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail().getValue(),
                user.getRole());
    }

    public static UserResponse mapToResponseFromDTO(UserDTO user) {
        if (user == null) {
            return null;
        }

        return new UserResponse(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole());
    }

    public static UserDTO mapToDTOFromModel(User user) {
        if (user == null) {
            return null;
        }

        return new UserDTO(
                user.getId().getValue(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail().getValue(),
                user.getPassword().getValue(),
                user.getRole());
    }

    public static UserDTO mapToDTOFromCreatedEvent(UserCreated user) {
        if (user == null) {
            return null;
        }

        return new UserDTO(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole());
    }

    public static UserDTO mapToDTOFromUpdatesEvent(UserUpdated user) {
        if (user == null) {
            return null;
        }

        return new UserDTO(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole());
    }
    
}