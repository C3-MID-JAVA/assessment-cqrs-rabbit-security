package ec.com.sofka.data;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request body for creating an user")
public class CreateUserRequest {


    @NotNull(message = "username can not be null")
    @Schema(description = "Unique username assigned to the user", example = "Anderson")
    private final String username;

    @NotNull(message = "password name can not be null")
    @Schema(description = "Password  for the user", example = "12345")
    private final String password;
    @NotNull(message = "password name can not be null")
    @Schema(description = "Roles  for the user", example = "ADMIN,USER")
    private final String roles;

    public CreateUserRequest(String username, String password, String roles) {
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
