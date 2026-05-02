package de.sagaweschaefer.flashcard.domain.service;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class SpacedRepetitionServiceTest {

    private static final ZoneId ZONE = ZoneOffset.UTC;
    private static final LocalDateTime FIXED_NOW =
            LocalDateTime.of(2026, 5, 2, 12, 0);

    private SpacedRepetitionService service() {
        Clock fixed = Clock.fixed(FIXED_NOW.atZone(ZONE).toInstant(), ZONE);
        return new SpacedRepetitionService(fixed);
    }

    // --- isDue ---

    @Test
    void isDue_levelZero_alwaysDue() {
        assertTrue(service().isDue(0, FIXED_NOW));
        assertTrue(service().isDue(0, null));
    }

    @Test
    void isDue_lastCorrectAtNull_alwaysDue() {
        assertTrue(service().isDue(3, null));
    }

    @Test
    void isDue_level1_dueAfterOneMinute() {
        SpacedRepetitionService s = service();
        assertFalse(s.isDue(1, FIXED_NOW.minusSeconds(30)));
        assertTrue(s.isDue(1, FIXED_NOW.minusMinutes(2)));
    }

    @Test
    void isDue_level4_dueAfterOneDay() {
        SpacedRepetitionService s = service();
        assertFalse(s.isDue(4, FIXED_NOW.minusHours(23)));
        assertTrue(s.isDue(4, FIXED_NOW.minusDays(2)));
    }

    @Test
    void isDue_level6_dueAfterOneMonth() {
        SpacedRepetitionService s = service();
        assertFalse(s.isDue(6, FIXED_NOW.minusDays(20)));
        assertTrue(s.isDue(6, FIXED_NOW.minusMonths(2)));
    }

    @Test
    void isDue_levelAboveMax_treatedAsMax() {
        SpacedRepetitionService s = service();
        // Level 99 wird auf 6 geclampt -> 1 Monat Intervall
        assertFalse(s.isDue(99, FIXED_NOW.minusDays(10)));
        assertTrue(s.isDue(99, FIXED_NOW.minusMonths(2)));
    }

    // --- applyRating ---

    @Test
    void applyRating_bad_decreasesLevelButNotBelowZero() {
        SpacedRepetitionService s = service();
        assertEquals(2, s.applyRating(3, SpacedRepetitionService.RATING_BAD, true));
        assertEquals(0, s.applyRating(0, SpacedRepetitionService.RATING_BAD, true));
    }

    @Test
    void applyRating_ok_keepsLevel() {
        SpacedRepetitionService s = service();
        assertEquals(4, s.applyRating(4, SpacedRepetitionService.RATING_OK, true));
        assertEquals(4, s.applyRating(4, SpacedRepetitionService.RATING_OK, false));
    }

    @Test
    void applyRating_good_increasesLevelOnlyWhenDue() {
        SpacedRepetitionService s = service();
        assertEquals(3, s.applyRating(2, SpacedRepetitionService.RATING_GOOD, true));
        assertEquals(2, s.applyRating(2, SpacedRepetitionService.RATING_GOOD, false));
    }

    @Test
    void applyRating_good_doesNotExceedMaxLevel() {
        SpacedRepetitionService s = service();
        assertEquals(SpacedRepetitionService.MAX_LEVEL,
                s.applyRating(SpacedRepetitionService.MAX_LEVEL,
                        SpacedRepetitionService.RATING_GOOD, true));
    }

    @Test
    void applyRating_unknownRating_keepsLevel() {
        SpacedRepetitionService s = service();
        assertEquals(2, s.applyRating(2, 99, true));
    }

    // --- nextDueDate ---

    @Test
    void nextDueDate_levelZero_returnsLastCorrect() {
        SpacedRepetitionService s = service();
        LocalDateTime last = FIXED_NOW.minusHours(1);
        assertEquals(last, s.nextDueDate(0, last));
    }

    @Test
    void nextDueDate_level3_addsFiveHours() {
        SpacedRepetitionService s = service();
        LocalDateTime last = FIXED_NOW.minusDays(1);
        assertEquals(last.plusHours(5), s.nextDueDate(3, last));
    }

    // --- levelAfterWrongAnswer ---

    @Test
    void levelAfterWrongAnswer_isMinLevel() {
        assertEquals(SpacedRepetitionService.MIN_LEVEL, service().levelAfterWrongAnswer());
    }

    // --- Konstruktor ---

    @Test
    void constructor_rejectsNullClock() {
        assertThrows(IllegalArgumentException.class, () -> new SpacedRepetitionService(null));
    }

    @Test
    void constructor_default_usesSystemClock() {
        // Smoke-Test: Default-Konstruktor erzeugt funktionsfähigen Service.
        SpacedRepetitionService s = new SpacedRepetitionService();
        assertTrue(s.isDue(0, null));
    }

    // --- Unveränderlichkeit der Eingaben ---

    @Test
    void nextDueDate_doesNotMutateInput() {
        SpacedRepetitionService s = service();
        LocalDateTime last = FIXED_NOW.minusDays(2);
        s.nextDueDate(4, last);
        // LocalDateTime ist immutable; der Aufruf darf den Wert nicht verändern.
        assertEquals(FIXED_NOW.minusDays(2), last);
    }

    @Test
    void clockInstantsAreUsedConsistently() {
        // Sicherstellt, dass der Service tatsächlich den injizierten Clock benutzt.
        Instant fixedInstant = Instant.parse("2026-05-02T00:00:00Z");
        Clock clock = Clock.fixed(fixedInstant, ZoneOffset.UTC);
        SpacedRepetitionService s = new SpacedRepetitionService(clock);

        LocalDateTime lastCorrect = LocalDateTime.ofInstant(fixedInstant, ZoneOffset.UTC).minusHours(6);
        // Level 3 => +5h -> liegt vor jetzt -> fällig
        assertTrue(s.isDue(3, lastCorrect));
    }
}

