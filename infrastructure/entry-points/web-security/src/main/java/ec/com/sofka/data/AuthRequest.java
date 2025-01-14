package ec.com.sofka.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request body for authenticate an user")
public class AuthRequest {

    @NotNull(message = "username can not be null")
    @NotBlank(message = "username can not be empty")
    @Schema(description = "Unique username assigned to the user", example = "testUser")
    private String username;

    @NotNull(message = "password name can not be null")
    @NotBlank(message = "password can not be empty")
    @Schema(description = "Password  for the user", example = "Abc123#*")
    private String password;

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
