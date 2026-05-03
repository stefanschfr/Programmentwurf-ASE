package de.sagaweschaefer.flashcard.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FlashcardTest {

    @Test
    void checkAnswer_freeText_correctAnswer() {
        Flashcard card = new Flashcard("Hauptstadt von Deutschland?", "Berlin");
        assertTrue(card.checkAnswer("Berlin"));
    }

    @Test
    void checkAnswer_freeText_caseInsensitive() {
        Flashcard card = new Flashcard("Hauptstadt von Deutschland?", "Berlin");
        assertTrue(card.checkAnswer("berlin"));
    }

    @Test
    void checkAnswer_freeText_wrongAnswer() {
        Flashcard card = new Flashcard("Hauptstadt von Deutschland?", "Berlin");
        assertFalse(card.checkAnswer("München"));
    }

    @Test
    void checkAnswer_multipleChoice_correct() {
        Flashcard card = new Flashcard("Farbe des Himmels?", "Blau", List.of("Blau", "Rot", "Grün"));
        assertTrue(card.checkAnswer("Blau"));
    }

    @Test
    void checkAnswer_trueFalse_wahr() {
        Flashcard card = new Flashcard("Die Erde ist rund", true);
        assertTrue(card.checkAnswer("wahr"));
        assertTrue(card.checkAnswer("w"));
    }

    @Test
    void checkAnswer_trueFalse_falsch() {
        Flashcard card = new Flashcard("Die Erde ist flach", false);
        assertTrue(card.checkAnswer("falsch"));
        assertTrue(card.checkAnswer("f"));
    }

    @Test
    void checkAnswer_numeric_correct() {
        Flashcard card = new Flashcard("Was ist 2+2?", 4.0);
        assertTrue(card.checkAnswer("4.0"));
        assertTrue(card.checkAnswer("4"));
    }

    @Test
    void checkAnswer_numeric_wrong() {
        Flashcard card = new Flashcard("Was ist 2+2?", 4.0);
        assertFalse(card.checkAnswer("5"));
    }

    @Test
    void checkAnswer_nullAnswer_returnsFalse() {
        Flashcard card = new Flashcard("Frage?", "Antwort");
        assertFalse(card.checkAnswer(null));
    }

    @Test
    void getCorrectAnswerDisplay_numeric() {
        Flashcard card = new Flashcard("Was ist Pi?", 3.14);
        assertEquals("3.14", card.getCorrectAnswerDisplay());
    }
}

