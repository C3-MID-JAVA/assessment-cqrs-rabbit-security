package ec.com.sofka.aggregates.account.entities.account.values.objects;

import ec.com.sofka.exceptions.InvalidFieldException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountNumberTest {

    @Test
    @DisplayName("when value is a valid 10-digit number should create AccountNumber instance successfully")
    void whenValueIsValidShouldCreateAccountNumberInstanceSuccessfully() {
        String validAccountNumber = "1234567890";
        AccountNumber accountNumber = AccountNumber.of(validAccountNumber);
        assertNotNull(accountNumber);
        assertEquals(validAccountNumber, accountNumber.getValue());
    }

    @Test
    @DisplayName("when value is null should throw InvalidFieldException")
    void whenValueIsNullShouldThrowInvalidFieldException() {
        String nullValue = null;
        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> AccountNumber.of(nullValue));
        assertEquals("Invalid number account.", exception.getMessage());
    }

    @Test
    @DisplayName("when value is not exactly 10 digits should throw InvalidFieldException")
    void whenValueIsNotTenDigitsShouldThrowInvalidFieldException() {
        String invalidAccountNumber = "123456789";
        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> AccountNumber.of(invalidAccountNumber));
        assertEquals("Invalid number account.", exception.getMessage());
    }

    @Test
    @DisplayName("when value contains non-digit characters should throw InvalidFieldException")
    void whenValueContainsNonDigitCharactersShouldThrowInvalidFieldException() {
        String invalidAccountNumber = "12345abc90";
        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> AccountNumber.of(invalidAccountNumber));
        assertEquals("Invalid number account.", exception.getMessage());
    }

    @Test
    @DisplayName("when value is an empty string should throw InvalidFieldException")
    void whenValueIsEmptyShouldThrowInvalidFieldException() {
        String emptyValue = "";
        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> AccountNumber.of(emptyValue));
        assertEquals("Invalid number account.", exception.getMessage());
    }

    @Test
    @DisplayName("when value contains spaces should throw InvalidFieldException")
    void whenValueContainsSpacesShouldThrowInvalidFieldException() {
        String invalidAccountNumber = "12345 67890";
        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> AccountNumber.of(invalidAccountNumber));
        assertEquals("Invalid number account.", exception.getMessage());
    }

}