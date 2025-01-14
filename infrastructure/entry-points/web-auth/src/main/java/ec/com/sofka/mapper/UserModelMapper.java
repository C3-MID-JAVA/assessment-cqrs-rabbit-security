package ec.com.sofka.mapper;

import ec.com.sofka.commands.UserCommand;
import ec.com.sofka.data.UserRequestDTO;
import ec.com.sofka.data.UserResponseDTO;
import ec.com.sofka.data.UserUpdateRequestDTO;
import ec.com.sofka.queries.responses.UserResponse;
import ec.com.sofka.utils.enums.RoleEnum;

public class UserModelMapper {

    public static UserCommand mapToRequest(UserRequestDTO user) {
        if (user == null) {
            return null;
        }

        return new UserCommand(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                RoleEnum.valueOf(user.getUserRole())
        );
    }

    public static UserCommand mapToUpdateRequest(UserUpdateRequestDTO user) {
        if (user == null) {
            return null;
        }

        return new UserCommand(
                user.getAggregateId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                RoleEnum.valueOf(user.getUserRole())
        );
    }

    public static UserResponseDTO mapToDTO(UserResponse user) {
        if (user == null) {
            return null;
        }

        return new UserResponseDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().name()
        );
    }

}