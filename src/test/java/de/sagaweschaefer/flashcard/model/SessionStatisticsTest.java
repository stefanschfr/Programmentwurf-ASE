package de.sagaweschaefer.flashcard.model;

import de.sagaweschaefer.flashcard.menu.flashcardsession.FlashcardSessionStatistics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionStatisticsTest {

    @Test
    void calculateGrade_100percent() {
        assertEquals(1.0, FlashcardSessionStatistics.calculateGrade(100));
    }

    @Test
    void calculateGrade_50percent() {
        assertEquals(4.0, FlashcardSessionStatistics.calculateGrade(50));
    }

    @Test
    void calculateGrade_0percent() {
        assertEquals(6.0, FlashcardSessionStatistics.calculateGrade(0));
    }

    @Test
    void formatTime_lessThanMinute() {
        String result = FlashcardSessionStatistics.formatTime(45000);
        assertEquals("0m 45s", result);
    }

    @Test
    void formatTime_multipleMinutes() {
        String result = FlashcardSessionStatistics.formatTime(125000);
        assertEquals("2m 5s", result);
    }
}

