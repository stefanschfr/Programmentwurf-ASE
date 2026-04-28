package de.sagaweschaefer.flashcard.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FlashcardIdTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void trimsAndComparesByExactValue() throws Exception {
        FlashcardId expected = FlashcardId.of("card-123");
        FlashcardId actual = FlashcardId.of("  card-123  ");

        assertEquals(expected, actual);
        assertEquals(expected.hashCode(), actual.hashCode());
        assertEquals("\"card-123\"", objectMapper.writeValueAsString(expected));
        assertEquals(expected, objectMapper.readValue("\"card-123\"", FlashcardId.class));
    }

    @Test
    void treatsDifferentIdsAsDifferentIdentities() {
        FlashcardId first = FlashcardId.of("card-123");
        FlashcardId second = FlashcardId.of("card-456");

        assertNotEquals(first, second);
        assertEquals("card-123", first.toString());
    }

    @Test
    void createsRandomIds() {
        FlashcardId first = FlashcardId.random();
        FlashcardId second = FlashcardId.random();

        assertFalse(first.value().isBlank());
        assertFalse(second.value().isBlank());
        assertNotEquals(first, second);
    }

    @Test
    void rejectsBlankIds() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> FlashcardId.of("   "));

        assertEquals("Die Flashcard-ID darf nicht leer sein.", exception.getMessage());
    }
}


