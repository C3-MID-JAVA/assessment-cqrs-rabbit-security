package ec.com.sofka.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

@Schema(description = "Object representing the data to create a customer.")
public class CustomerRequestDTO implements Serializable {

    @Schema(description = "First name of customer", example = "Juan")
    @NotNull(message = "firstName cannot be null")
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúñÑ ]+$", message = "Incorrect firstName format")
    private String firstName;

    @Schema(description = "Last name of customer", example = "Zambrano")
    @NotNull(message = "lastName cannot be null")
    @Pattern(regexp = "^[a-zA-ZÁÉÍÓÚáéíóúñÑ ]+$", message = "Incorrect lastName format")
    private String lastName;

    @Schema(description = "Identification code of customer", example = "1000000000")
    @NotNull(message = "identity card cannot be null")
    @Pattern(regexp = "^[0-9]+$", message = "Incorrect identity card format")
//    @Size(min = 10, max = 10, message = "Incorrect identity card length")
    private String identityCard;

    @Schema(description = "Status of customer", example = "ACTIVE")
    @NotNull(message = "status cannot be null")
    @Pattern(regexp = "^(ACTIVE|INACTIVE)$", message = "Incorrect status")
    private String status;

    public CustomerRequestDTO() {
    }

    public CustomerRequestDTO(String firstName, String lastName, String identityCard, String email, String password, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.identityCard = identityCard;
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public String getStatus() {
        return status;
    }

}