package ec.com.sofka.mapper;

import ec.com.sofka.data.UserEntity;
import ec.com.sofka.gateway.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {
    public static UserEntity mapToEntity(UserDTO user) {
        if (user == null) {
            return null;
        }
        return new UserEntity(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getRole());
    }

    public static UserDTO mapToDTO(UserEntity user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getPassword(),
                user.getRole()
        );
    }

}