package ec.com.sofka.aggregates.account.entities.transactions.values.objects;

import ec.com.sofka.aggregates.account.entities.transaction.values.objects.TransactionAccount;
import ec.com.sofka.exceptions.InvalidFieldException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionAccountTest {

    @Test
    @DisplayName("when value is valid should create TransactionAccount successfully")
    void whenValueIsValidShouldCreateTransactionAccountSuccessfully() {
        String validAccountNumber = "1234567890";
        TransactionAccount transactionAccount = TransactionAccount.of(validAccountNumber);
        assertNotNull(transactionAccount);
        assertEquals(validAccountNumber, transactionAccount.getValue());
    }

    @Test
    @DisplayName("when value is null should throw InvalidFieldException")
    void whenValueIsNullShouldThrowInvalidFieldException() {
        String invalidAccountNumber = null;
        assertThrows(InvalidFieldException.class, () -> TransactionAccount.of(invalidAccountNumber));
    }

    @Test
    @DisplayName("when value is empty should throw InvalidFieldException")
    void whenValueIsEmptyShouldThrowInvalidFieldException() {
        String invalidAccountNumber = "";
        assertThrows(InvalidFieldException.class, () -> TransactionAccount.of(invalidAccountNumber));
    }

    @Test
    @DisplayName("when value is less than 10 digits should throw InvalidFieldException")
    void whenValueIsLessThan10DigitsShouldThrowInvalidFieldException() {
        String invalidAccountNumber = "12345";
        assertThrows(InvalidFieldException.class, () -> TransactionAccount.of(invalidAccountNumber));
    }

    @Test
    @DisplayName("when value is more than 10 digits should throw InvalidFieldException")
    void whenValueIsMoreThan10DigitsShouldThrowInvalidFieldException() {
        String invalidAccountNumber = "123456789012";
        assertThrows(InvalidFieldException.class, () -> TransactionAccount.of(invalidAccountNumber));
    }

    @Test
    @DisplayName("when value contains non-digit characters should throw InvalidFieldException")
    void whenValueContainsNonDigitCharactersShouldThrowInvalidFieldException() {
        String invalidAccountNumber = "12345ABCDE";
        assertThrows(InvalidFieldException.class, () -> TransactionAccount.of(invalidAccountNumber));
    }

}