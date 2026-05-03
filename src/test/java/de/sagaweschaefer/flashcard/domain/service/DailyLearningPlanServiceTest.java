package de.sagaweschaefer.flashcard.domain.service;

import de.sagaweschaefer.flashcard.domain.valueobject.DailyGoal;
import de.sagaweschaefer.flashcard.model.DailyProgress;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;
import de.sagaweschaefer.flashcard.testdoubles.FakeDailyGoalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DailyLearningPlanServiceTest {

    private static final LocalDateTime FIXED_NOW = LocalDateTime.of(2026, 5, 2, 9, 0);
    private static final Clock FIXED_CLOCK = Clock.fixed(FIXED_NOW.toInstant(ZoneOffset.UTC), ZoneOffset.UTC);

    private FakeDailyGoalRepository repository;
    private SpacedRepetitionService spacedRepetitionService;
    private DailyLearningPlanService service;

    @BeforeEach
    void setUp() {
        repository = new FakeDailyGoalRepository();
        spacedRepetitionService = new SpacedRepetitionService(FIXED_CLOCK);
        service = new DailyLearningPlanService(repository, spacedRepetitionService, FIXED_CLOCK);
    }

    @Test
    void getGoalOrDefault_noGoalSet_returnsDefault() {
        assertEquals(DailyGoal.DEFAULT_CARDS, service.getGoalOrDefault().getCardsPerDay());
    }

    @Test
    void setGoal_persistsGoal() {
        service.setGoal(new DailyGoal(15));
        assertEquals(15, service.getGoalOrDefault().getCardsPerDay());
    }

    @Test
    void setGoal_rejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> service.setGoal(null));
    }

    @Test
    void getOrCreateTodayProgress_createsAndPersistsWhenAbsent() {
        DailyProgress p = service.getOrCreateTodayProgress();
        assertEquals(LocalDate.of(2026, 5, 2), p.getDate());
        assertEquals(DailyGoal.DEFAULT_CARDS, p.getGoalCards());
        assertEquals(1, repository.progressEntryCount());
    }

    @Test
    void getOrCreateTodayProgress_returnsExistingForSameDay() {
        DailyProgress first = service.getOrCreateTodayProgress();
        first.recordCard(true);
        repository.saveProgress(first);

        DailyProgress second = service.getOrCreateTodayProgress();
        assertEquals(1, second.getLearnedCards());
    }

    @Test
    void recordCardLearned_updatesCountersAndPersists() {
        DailyProgress p1 = service.recordCardLearned(true);
        DailyProgress p2 = service.recordCardLearned(false);
        assertEquals(2, p2.getLearnedCards());
        assertEquals(1, p2.getCorrectCards());
        // identische Tagesentity
        assertSame(p1, p2);
    }

    @Test
    void recommendCardsForToday_emptySets_returnsEmptyList() {
        List<Flashcard> recs = service.recommendCardsForToday(List.of(), Map.of());
        assertTrue(recs.isEmpty());
    }

    @Test
    void recommendCardsForToday_prefersDueOverNew_andRespectsRemainingGoal() {
        service.setGoal(new DailyGoal(2));

        // Set mit 3 Karten: due, new, brand-new
        FlashcardSet set = new FlashcardSet("Set");
        Flashcard dueCard = new Flashcard("Q1", "A1");
        Flashcard newCard1 = new Flashcard("Q2", "A2");
        Flashcard newCard2 = new Flashcard("Q3", "A3");
        set.getFlashcards().add(dueCard);
        set.getFlashcards().add(newCard1);
        set.getFlashcards().add(newCard2);

        // Statistics: dueCard hat ein Level 1 mit altem lastCorrect -> fällig
        Map<String, FlashcardStatistics> stats = new HashMap<>();
        FlashcardStatistics s = new FlashcardStatistics(dueCard.getId());
        s.setLevel(1);
        s.setLastCorrectAt(FIXED_NOW.minusHours(1)); // Level 1 -> 1 Min Intervall, also fällig
        stats.put(dueCard.getId(), s);

        List<Flashcard> recs = service.recommendCardsForToday(List.of(set), stats);

        assertEquals(2, recs.size());
        // erste Empfehlung muss fällige Karte sein
        assertEquals(dueCard.getId(), recs.get(0).getId());
        // zweite muss eine der neuen Karten sein
        assertNotEquals(dueCard.getId(), recs.get(1).getId());
    }

    @Test
    void recommendCardsForToday_goalAlreadyReached_returnsEmpty() {
        service.setGoal(new DailyGoal(1));
        DailyProgress p = service.getOrCreateTodayProgress();
        p.recordCard(true); // Ziel erreicht

        FlashcardSet set = new FlashcardSet("Set");
        set.getFlashcards().add(new Flashcard("Q", "A"));

        List<Flashcard> recs = service.recommendCardsForToday(List.of(set), Map.of());
        assertTrue(recs.isEmpty());
    }

    @Test
    void recommendCardsForToday_deduplicatesAcrossSets() {
        service.setGoal(new DailyGoal(10));
        Flashcard card = new Flashcard("Q", "A");
        FlashcardSet a = new FlashcardSet("A");
        FlashcardSet b = new FlashcardSet("B");
        a.getFlashcards().add(card);
        b.getFlashcards().add(card);

        List<Flashcard> recs = service.recommendCardsForToday(List.of(a, b), Map.of());
        assertEquals(1, recs.size());
    }

    @Test
    void constructor_rejectsNullArgs() {
        assertThrows(IllegalArgumentException.class,
                () -> new DailyLearningPlanService(null, spacedRepetitionService, FIXED_CLOCK));
        assertThrows(IllegalArgumentException.class,
                () -> new DailyLearningPlanService(repository, null, FIXED_CLOCK));
        assertThrows(IllegalArgumentException.class,
                () -> new DailyLearningPlanService(repository, spacedRepetitionService, null));
    }
}

