package ec.com.sofka.mapper;

import ec.com.sofka.dto.UserRequestDTO;
import ec.com.sofka.dto.UserResponseDTO;
import ec.com.sofka.user.request.CreateUserRequest;
import ec.com.sofka.user.responses.UserResponse;

public class UserMapper {
    public static UserResponseDTO fromEntity(UserResponse userResponse) {
        return new UserResponseDTO(userResponse.getCustomerId(), userResponse.getName(), userResponse.getDocumentId());
    }

    public static CreateUserRequest toEntity(UserRequestDTO userRequestDTO) {
        return new CreateUserRequest(userRequestDTO.getName(), userRequestDTO.getDocumentId());
    }
}
