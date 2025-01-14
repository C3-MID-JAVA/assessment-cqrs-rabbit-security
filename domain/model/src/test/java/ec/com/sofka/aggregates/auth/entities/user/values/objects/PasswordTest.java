package ec.com.sofka.aggregates.auth.entities.user.values.objects;

import ec.com.sofka.exceptions.InvalidFieldException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordTest {
    @Test
    @DisplayName("when value is valid should create Password instance successfully")
    void whenValueIsValidShouldCreatePasswordInstanceSuccessfully() {
        String validPassword = "Secure1@";

        Password password = Password.of(validPassword);

        assertNotNull(password);
        assertEquals(validPassword, password.getValue());
    }

    @Test
    @DisplayName("when value is null should throw InvalidFieldException")
    void whenValueIsNullShouldThrowInvalidFieldException() {
        String nullValue = null;

        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> Password.of(nullValue));
        assertEquals("Password cannot be null or blank.", exception.getMessage());
    }

    @Test
    @DisplayName("when value is blank should throw InvalidFieldException")
    void whenValueIsBlankShouldThrowInvalidFieldException() {
        String blankValue = "   ";

        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> Password.of(blankValue));
        assertEquals("Password cannot be null or blank.", exception.getMessage());
    }

    @Test
    @DisplayName("when value does not contain an uppercase letter should throw InvalidFieldException")
    void whenValueDoesNotContainUppercaseShouldThrowInvalidFieldException() {
        String passwordWithoutUppercase = "secure1@";

        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> Password.of(passwordWithoutUppercase));
        assertEquals("The password is not secure.", exception.getMessage());
    }

    @Test
    @DisplayName("when value does not contain a digit should throw InvalidFieldException")
    void whenValueDoesNotContainDigitShouldThrowInvalidFieldException() {
        String passwordWithoutDigit = "Secure@";

        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> Password.of(passwordWithoutDigit));
        assertEquals("The password is not secure.", exception.getMessage());
    }

    @Test
    @DisplayName("when value does not contain a special character should throw InvalidFieldException")
    void whenValueDoesNotContainSpecialCharacterShouldThrowInvalidFieldException() {
        String passwordWithoutSpecialChar = "Secure1";

        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> Password.of(passwordWithoutSpecialChar));
        assertEquals("The password is not secure.", exception.getMessage());
    }

    @Test
    @DisplayName("when value meets minimum requirements should create Password instance successfully")
    void whenValueMeetsMinimumRequirementsShouldCreatePasswordInstanceSuccessfully() {
        String validPassword = "A1@";

        Password password = Password.of(validPassword);

        assertNotNull(password);
        assertEquals(validPassword, password.getValue());
    }

    @Test
    @DisplayName("when value is too short but meets regex should create Password instance successfully")
    void whenValueIsTooShortButMeetsRegexShouldCreatePasswordInstanceSuccessfully() {
        String shortValidPassword = "A1@";

        Password password = Password.of(shortValidPassword);

        assertNotNull(password);
        assertEquals(shortValidPassword, password.getValue());
    }
}
