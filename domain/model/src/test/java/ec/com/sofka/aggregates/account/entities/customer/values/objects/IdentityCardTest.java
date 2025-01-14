package ec.com.sofka.aggregates.account.entities.customer.values.objects;

import ec.com.sofka.exceptions.InvalidFieldException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IdentityCardTest {

    @Test
    @DisplayName("when value is a valid 10-digit number should create IdentityCard instance successfully")
    void whenValueIsValidShouldCreateIdentityCardInstanceSuccessfully() {
        String validIdentityCard = "1234567890";
        IdentityCard identityCard = IdentityCard.of(validIdentityCard);
        assertNotNull(identityCard);
        assertEquals(validIdentityCard, identityCard.getValue());
    }

    @Test
    @DisplayName("when value is null should throw InvalidFieldException")
    void whenValueIsNullShouldThrowInvalidFieldException() {
        String nullValue = null;
        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> IdentityCard.of(nullValue));
        assertEquals("Invalid identity card number.", exception.getMessage());
    }

    @Test
    @DisplayName("when value is not exactly 10 digits should throw InvalidFieldException")
    void whenValueIsNotTenDigitsShouldThrowInvalidFieldException() {
        String invalidIdentityCard = "123456789";
        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> IdentityCard.of(invalidIdentityCard));
        assertEquals("Invalid identity card number.", exception.getMessage());
    }

    @Test
    @DisplayName("when value contains non-digit characters should throw InvalidFieldException")
    void whenValueContainsNonDigitCharactersShouldThrowInvalidFieldException() {
        String invalidIdentityCard = "12345abc90";
        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> IdentityCard.of(invalidIdentityCard));
        assertEquals("Invalid identity card number.", exception.getMessage());
    }

    @Test
    @DisplayName("when value is an empty string should throw InvalidFieldException")
    void whenValueIsEmptyShouldThrowInvalidFieldException() {
        String emptyValue = "";
        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> IdentityCard.of(emptyValue));
        assertEquals("Invalid identity card number.", exception.getMessage());
    }

    @Test
    @DisplayName("when value contains spaces should throw InvalidFieldException")
    void whenValueContainsSpacesShouldThrowInvalidFieldException() {
        String invalidIdentityCard = "12345 67890";
        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> IdentityCard.of(invalidIdentityCard));
        assertEquals("Invalid identity card number.", exception.getMessage());
    }

}
