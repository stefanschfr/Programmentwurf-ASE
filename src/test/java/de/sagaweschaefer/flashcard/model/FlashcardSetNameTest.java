package de.sagaweschaefer.flashcard.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FlashcardSetNameTest {
    @Test
    void trimsDisplayValueAndComparesIgnoringCase() {
        FlashcardSetName expected = FlashcardSetName.of("Java Basics");
        FlashcardSetName actual = FlashcardSetName.of("  java basics  ");

        assertEquals("Java Basics", expected.value());
        assertEquals(expected, actual);
        assertEquals(expected.hashCode(), actual.hashCode());
        assertTrue(expected.matches(" JAVA BASICS "));
    }

    @Test
    void preservesOriginalDisplayCasingForDifferentSemanticNames() {
        FlashcardSetName java = FlashcardSetName.of("Java");
        FlashcardSetName python = FlashcardSetName.of("Python");

        assertNotEquals(java, python);
        assertEquals("Java", java.toString());
    }

    @Test
    void rejectsBlankNames() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> FlashcardSetName.of("   "));

        assertEquals("Der Name des Lernkartensets darf nicht leer sein.", exception.getMessage());
    }
}

