package de.sagaweschaefer.flashcard.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlashcardStatisticsTest {

    @Test
    void newStatistics_isDue() {
        FlashcardStatistics stats = new FlashcardStatistics("id1");
        assertTrue(stats.isDue());
    }

    @Test
    void incrementCorrect_updatesCount() {
        FlashcardStatistics stats = new FlashcardStatistics("id1");
        stats.incrementCorrect();
        assertEquals(1, stats.getCorrectCount());
        assertNotNull(stats.getLastCorrectAt());
    }

    @Test
    void incrementWrong_resetsLevel() {
        FlashcardStatistics stats = new FlashcardStatistics("id1");
        stats.setLevel(3);
        stats.incrementWrong();
        assertEquals(0, stats.getLevel());
        assertEquals(1, stats.getWrongCount());
    }

    @Test
    void applyRating_ratingOne_decreasesLevel() {
        FlashcardStatistics stats = new FlashcardStatistics("id1");
        stats.setLevel(3);
        stats.applyRating(1, true);
        assertEquals(2, stats.getLevel());
    }

    @Test
    void applyRating_ratingThree_increasesLevelWhenDue() {
        FlashcardStatistics stats = new FlashcardStatistics("id1");
        stats.setLevel(2);
        stats.applyRating(3, true);
        assertEquals(3, stats.getLevel());
    }

    @Test
    void applyRating_ratingThree_doesNotIncreaseLevelWhenNotDue() {
        FlashcardStatistics stats = new FlashcardStatistics("id1");
        stats.setLevel(2);
        stats.applyRating(3, false);
        assertEquals(2, stats.getLevel());
    }

    @Test
    void applyRating_ratingTwo_levelUnchanged() {
        FlashcardStatistics stats = new FlashcardStatistics("id1");
        stats.setLevel(3);
        stats.applyRating(2, true);
        assertEquals(3, stats.getLevel());
    }

    @Test
    void level_cannotExceedSix() {
        FlashcardStatistics stats = new FlashcardStatistics("id1");
        stats.setLevel(6);
        stats.applyRating(3, true);
        assertEquals(6, stats.getLevel());
    }

    @Test
    void level_cannotGoBelowZero() {
        FlashcardStatistics stats = new FlashcardStatistics("id1");
        stats.setLevel(0);
        stats.applyRating(1, true);
        assertEquals(0, stats.getLevel());
    }
}

