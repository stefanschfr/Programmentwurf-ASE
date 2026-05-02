package de.sagaweschaefer.flashcard.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DailyGoalTest {

    @Test
    void constructor_acceptsValidValue() {
        DailyGoal goal = new DailyGoal(25);
        assertEquals(25, goal.getCardsPerDay());
    }

    @Test
    void constructor_rejectsZero() {
        assertThrows(IllegalArgumentException.class, () -> new DailyGoal(0));
    }

    @Test
    void constructor_rejectsNegative() {
        assertThrows(IllegalArgumentException.class, () -> new DailyGoal(-1));
    }

    @Test
    void constructor_rejectsTooLarge() {
        assertThrows(IllegalArgumentException.class, () -> new DailyGoal(DailyGoal.MAX_CARDS + 1));
    }

    @Test
    void defaultGoal_returnsDefaultCards() {
        assertEquals(DailyGoal.DEFAULT_CARDS, DailyGoal.defaultGoal().getCardsPerDay());
    }

    @Test
    void equality_byValue() {
        DailyGoal a = new DailyGoal(20);
        DailyGoal b = new DailyGoal(20);
        DailyGoal c = new DailyGoal(21);
        assertEquals(a, b);
        assertNotEquals(a, c);
    }

    @Test
    void hashCode_byValue() {
        DailyGoal a = new DailyGoal(20);
        DailyGoal b = new DailyGoal(20);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void toString_containsValue() {
        assertTrue(new DailyGoal(7).toString().contains("7"));
    }
}

