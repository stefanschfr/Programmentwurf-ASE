package de.sagaweschaefer.flashcard.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DailyProgressTest {

    @Test
    void newProgress_hasZeroCounts() {
        DailyProgress p = new DailyProgress(LocalDate.now(), 10);
        assertEquals(0, p.getLearnedCards());
        assertEquals(0, p.getCorrectCards());
        assertFalse(p.isGoalReached());
    }

    @Test
    void recordCard_correct_incrementsBothCounters() {
        DailyProgress p = new DailyProgress(LocalDate.now(), 10);
        p.recordCard(true);
        assertEquals(1, p.getLearnedCards());
        assertEquals(1, p.getCorrectCards());
    }

    @Test
    void recordCard_wrong_onlyIncrementsLearned() {
        DailyProgress p = new DailyProgress(LocalDate.now(), 10);
        p.recordCard(false);
        assertEquals(1, p.getLearnedCards());
        assertEquals(0, p.getCorrectCards());
    }

    @Test
    void isGoalReached_whenLearnedReachesGoal() {
        DailyProgress p = new DailyProgress(LocalDate.now(), 2);
        p.recordCard(true);
        assertFalse(p.isGoalReached());
        p.recordCard(false);
        assertTrue(p.isGoalReached());
    }

    @Test
    void progressRatio_isClampedToOne() {
        DailyProgress p = new DailyProgress(LocalDate.now(), 2);
        p.recordCard(true);
        p.recordCard(true);
        p.recordCard(true); // übererfüllt
        assertEquals(1.0, p.getProgressRatio(), 1e-9);
    }

    @Test
    void progressRatio_zeroGoal_returnsZero() {
        DailyProgress p = new DailyProgress(LocalDate.now(), 0);
        p.recordCard(true);
        assertEquals(0.0, p.getProgressRatio(), 1e-9);
    }

    @Test
    void progressRatio_intermediate() {
        DailyProgress p = new DailyProgress(LocalDate.now(), 4);
        p.recordCard(true);
        assertEquals(0.25, p.getProgressRatio(), 1e-9);
    }
}

