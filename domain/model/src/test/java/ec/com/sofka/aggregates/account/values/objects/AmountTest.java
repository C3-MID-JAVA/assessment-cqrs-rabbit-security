package ec.com.sofka.aggregates.account.values.objects;

import ec.com.sofka.exceptions.InvalidFieldException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AmountTest {

    @Test
    @DisplayName("when value is valid should create Amount instance successfully")
    void whenValueIsValidShouldCreateAmountInstanceSuccessfully() {
        BigDecimal validValue = new BigDecimal("100.50");
        Amount amount = Amount.of(validValue);
        assertNotNull(amount);
        assertEquals(validValue, amount.getValue());
    }

    @Test
    @DisplayName("when value is null should throw InvalidFieldException")
    void whenValueIsNullShouldThrowInvalidFieldException() {
        BigDecimal nullValue = null;
        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> Amount.of(nullValue));
        assertEquals("Amount cannot be null.", exception.getMessage());
    }

    @Test
    @DisplayName("when value is negative should throw InvalidFieldException")
    void whenValueIsNegativeShouldThrowInvalidFieldException() {
        BigDecimal negativeValue = new BigDecimal("-50.00");
        InvalidFieldException exception = assertThrows(InvalidFieldException.class, () -> Amount.of(negativeValue));
        assertEquals("Amount cannot be negative.", exception.getMessage());
    }

    @Test
    @DisplayName("when value is zero should create Amount instance successfully")
    void whenValueIsZeroShouldCreateAmountInstanceSuccessfully() {
        BigDecimal zeroValue = BigDecimal.ZERO;
        Amount amount = Amount.of(zeroValue);
        assertNotNull(amount);
        assertEquals(zeroValue, amount.getValue());
    }

    @Test
    @DisplayName("when value is a very large number should create Amount instance successfully")
    void whenValueIsVeryLargeShouldCreateAmountInstanceSuccessfully() {
        BigDecimal largeValue = new BigDecimal("1000000000000000000.00");
        Amount amount = Amount.of(largeValue);
        assertNotNull(amount);
        assertEquals(largeValue, amount.getValue());
    }

    @Test
    @DisplayName("when value has many decimal places should create Amount instance successfully")
    void whenValueHasManyDecimalPlacesShouldCreateAmountInstanceSuccessfully() {
        BigDecimal preciseValue = new BigDecimal("123.456789");
        Amount amount = Amount.of(preciseValue);
        assertNotNull(amount);
        assertEquals(preciseValue, amount.getValue());
    }

}