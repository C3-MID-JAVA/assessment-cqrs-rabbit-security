package ec.com.sofka.mapper;

import ec.com.sofka.data.ROLE;
import ec.com.sofka.data.UserEntity;
import ec.com.sofka.usecases.gateway.UserDTO;

public class UserMapper {
    public static UserEntity toUser(final UserDTO dto) {
        return new UserEntity(
                null,
                dto.getUsername(),
                dto.getPassword(),
                ROLE.valueOf(dto.getRoles())
        );
    }

    public static UserDTO toUserDTO(final UserEntity entity) {
        return new UserDTO(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getRoles().name()
        );
    }
}
