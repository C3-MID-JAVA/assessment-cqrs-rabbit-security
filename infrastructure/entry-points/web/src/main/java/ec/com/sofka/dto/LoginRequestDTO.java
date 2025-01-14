package ec.com.sofka.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class LoginRequestDTO {

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be correct format")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    public LoginRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
