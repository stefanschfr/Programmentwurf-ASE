package de.sagaweschaefer.flashcard.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlashcardIdTest {

    @Test
    void equality_sameValue() {
        FlashcardId id1 = new FlashcardId("abc-123");
        FlashcardId id2 = new FlashcardId("abc-123");
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertEquals("abc-123", id1.getValue());
    }

    @Test
    void inequality_differentValue() {
        FlashcardId id1 = new FlashcardId("abc-123");
        FlashcardId id2 = new FlashcardId("def-456");
        assertNotEquals(id1, id2);
    }

    @Test
    void constructor_throwsOnNull() {
        assertThrows(IllegalArgumentException.class, () -> new FlashcardId(null));
    }

    @Test
    void constructor_throwsOnBlank() {
        assertThrows(IllegalArgumentException.class, () -> new FlashcardId("  "));
    }

    @Test
    void generate_createsUniqueIds() {
        FlashcardId id1 = FlashcardId.generate();
        FlashcardId id2 = FlashcardId.generate();
        assertNotEquals(id1, id2);
    }
}

