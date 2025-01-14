package ec.com.sofka.mapper;

import ec.com.sofka.data.UserEntity;
import ec.com.sofka.gateway.dto.UserDTO;

public class UserMapperEntity {
    public static UserEntity toEntity(UserDTO userDTO){
        return new UserEntity(userDTO.getId(), userDTO.getName(), userDTO.getDocumentId());
    }

    public static UserDTO fromEntity(UserEntity user){
        return new UserDTO(user.getId(), user.getName(), user.getDocumentId());
    }
}
