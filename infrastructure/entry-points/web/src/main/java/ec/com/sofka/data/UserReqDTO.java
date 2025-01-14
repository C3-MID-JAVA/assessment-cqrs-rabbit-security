package ec.com.sofka.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request body for creating an user")
public class UserReqDTO {


    @NotNull(message = "username can not be null")
    @Schema(description = "Unique username assigned to the user", example = "testUser")
    private final String username;

    @NotNull(message = "password name can not be null")
    @Schema(description = "Password  for the user", example = "Abc123#*")
    private final String password;

    private final String roles;

    public UserReqDTO(String username, String password, String roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRoles() {
        return roles;
    }
}
