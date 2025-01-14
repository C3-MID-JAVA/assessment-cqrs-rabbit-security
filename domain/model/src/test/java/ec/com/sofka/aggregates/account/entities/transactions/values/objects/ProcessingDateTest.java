package ec.com.sofka.aggregates.account.entities.transactions.values.objects;

import ec.com.sofka.aggregates.account.entities.transaction.values.objects.ProcessingDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProcessingDateTest {

    @Test
    @DisplayName("when value is valid should create ProcessingDate successfully")
    void whenValueIsValidShouldCreateProcessingDateSuccessfully() {
        String validDate = "10-01-2025 14:30:45";
        ProcessingDate processingDate = ProcessingDate.of(validDate);
        assertNotNull(processingDate);
        assertEquals(validDate, processingDate.getValue());
    }

    @Test
    @DisplayName("when value is null should set current date and time")
    void whenValueIsNullShouldSetCurrentDateAndTime() {
        ProcessingDate processingDate = ProcessingDate.of(null);

        String currentDateTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        assertNotNull(processingDate);
        assertEquals(currentDateTime, processingDate.getValue());
    }

    @Test
    @DisplayName("when value is empty should set current date and time")
    void whenValueIsEmptyShouldSetCurrentDateAndTime() {
        ProcessingDate processingDate = ProcessingDate.of("");

        String currentDateTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        assertNotNull(processingDate);
        assertEquals(currentDateTime, processingDate.getValue());
    }

    @Test
    @DisplayName("when value is valid date format should preserve input value")
    void whenValueIsValidDateFormatShouldPreserveInputValue() {
        String validDate = "01-01-2024 12:00:00";
        ProcessingDate processingDate = ProcessingDate.of(validDate);

        assertNotNull(processingDate);
        assertEquals(validDate, processingDate.getValue());
    }

}