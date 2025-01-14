package ec.com.sofka.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

@Schema(description = "Object representing the data to create a user.")
public class UserRequestDTO implements Serializable {
    @Schema(description = "Email of user", example = "user@gmail.com")
    @NotNull(message = "email cannot be null")
    @Email( message = "Invalid email")
    private String email;

    @Schema(description = "Password of user", example = "User123.")
    @NotNull(message = "password cannot be null")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).+$", message = "Incorrect password format")
    @Size(min = 8, max = 16, message = "Incorrect password length")
    private String password;

    @Schema(description = "First name of user", example = "Juan")
    @NotNull(message = "firstName cannot be null")
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúñÑ ]+$", message = "Incorrect firstName format")
    private String firstName;

    @Schema(description = "Last name of user", example = "Zambrano")
    @NotNull(message = "lastName cannot be null")
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúñÑ ]+$", message = "Incorrect firstName format")
    private String lastName;

    @Schema(description = "User role of user", example = "ADMIN")
    @NotNull(message = "userRole cannot be null")
    private String userRole;

    public UserRequestDTO(String email, String password, String firstName, String lastName, String userRole) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userRole = userRole;
    }

    public UserRequestDTO() {
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

}