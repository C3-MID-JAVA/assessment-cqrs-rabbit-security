package ec.com.sofka.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Object representing the data to auth of user.")
public class AuthRequestDTO {
    @Schema(description = "Email of user", example = "user@gmail.com")
    @NotNull(message = "email cannot be null")
    @Email( message = "Invalid email")
    private String email;

    @Schema(description = "Password of user", example = "User123.")
    @NotNull(message = "password cannot be null")
    @Size(min = 8, max = 16, message = "Incorrect password length")
    private String password;

    public AuthRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public AuthRequestDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}