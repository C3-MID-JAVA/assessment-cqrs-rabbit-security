package ec.com.sofka.mapper;

import ec.com.sofka.dto.UserQueryResponseDTO;
import ec.com.sofka.dto.UserRequestDTO;
import ec.com.sofka.dto.UserResponseDTO;
import ec.com.sofka.user.commands.CreateUserCommand;
import ec.com.sofka.user.queries.responses.UserResponse;

public class UserMapper {
    public static UserResponseDTO fromEntity(UserResponse userResponse) {
        return new UserResponseDTO(userResponse.getId(), userResponse.getName(), userResponse.getDocumentId(), userResponse.getCustomerId());
    }

    public static UserQueryResponseDTO fromQueryEntity(UserResponse userResponse) {
        return new UserQueryResponseDTO(userResponse.getId(), userResponse.getName(), userResponse.getDocumentId());
    }

    public static CreateUserCommand toEntity(UserRequestDTO userRequestDTO) {
        return new CreateUserCommand(userRequestDTO.getName(), userRequestDTO.getDocumentId());
    }
}
