package ec.com.sofka.aggregates.auth.entities.user.values.objects;

import ec.com.sofka.exceptions.InvalidFieldException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmailTest {

    @Test
    @DisplayName("when value is valid should create Email instance successfully")
    void whenValueIsValidShouldCreateEmailInstanceSuccessfully() {
        String validEmail = "example@test.com";

        Email email = Email.of(validEmail);

        assertNotNull(email);
        assertEquals(validEmail, email.getValue());
    }

    @Test
    @DisplayName("when value is null should throw InvalidFieldException")
    void whenValueIsNullShouldThrowInvalidFieldException() {
        String nullValue = null;

        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> Email.of(nullValue));
        assertEquals("Email cannot be null or blank.", exception.getMessage());
    }

    @Test
    @DisplayName("when value is blank should throw InvalidFieldException")
    void whenValueIsBlankShouldThrowInvalidFieldException() {
        String blankValue = "   ";

        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> Email.of(blankValue));
        assertEquals("Email cannot be null or blank.", exception.getMessage());
    }

    @Test
    @DisplayName("when value is invalid format should throw InvalidFieldException")
    void whenValueIsInvalidFormatShouldThrowInvalidFieldException() {
        String invalidEmail = "invalid-email";

        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> Email.of(invalidEmail));
        assertEquals("Invalid email format.", exception.getMessage());
    }

    @Test
    @DisplayName("when value contains spaces should throw InvalidFieldException")
    void whenValueContainsSpacesShouldThrowInvalidFieldException() {
        String invalidEmailWithSpaces = "example @test.com";

        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> Email.of(invalidEmailWithSpaces));
        assertEquals("Invalid email format.", exception.getMessage());
    }

    @Test
    @DisplayName("when value contains special characters should throw InvalidFieldException")
    void whenValueContainsSpecialCharactersShouldThrowInvalidFieldException() {
        String invalidEmailWithSpecialChars = "exa(mple@test.com";

        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> Email.of(invalidEmailWithSpecialChars));
        assertEquals("Invalid email format.", exception.getMessage());
    }

    @Test
    @DisplayName("when value is uppercase should create Email instance successfully")
    void whenValueIsUppercaseShouldCreateEmailInstanceSuccessfully() {
        String uppercaseEmail = "EXAMPLE@TEST.COM";

        Email email = Email.of(uppercaseEmail);

        assertNotNull(email);
        assertEquals(uppercaseEmail, email.getValue());
    }

}
