package de.sagaweschaefer.flashcard.adapter.persistence;

import de.sagaweschaefer.flashcard.domain.valueobject.DailyGoal;
import de.sagaweschaefer.flashcard.model.DailyProgress;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JsonDailyGoalRepositoryTest {

    @Test
    void saveAndLoadGoal_roundTrip(@TempDir Path tmp) {
        Path file = tmp.resolve("daily-goal.json");
        JsonDailyGoalRepository repo = new JsonDailyGoalRepository(file.toString());

        repo.saveGoal(new DailyGoal(42));

        Optional<DailyGoal> loaded = new JsonDailyGoalRepository(file.toString()).findGoal();
        assertTrue(loaded.isPresent());
        assertEquals(42, loaded.get().getCardsPerDay());
    }

    @Test
    void saveAndLoadProgress_roundTrip(@TempDir Path tmp) {
        Path file = tmp.resolve("daily-goal.json");
        JsonDailyGoalRepository repo = new JsonDailyGoalRepository(file.toString());

        DailyProgress p = new DailyProgress(LocalDate.of(2026, 5, 2), 10);
        p.recordCard(true);
        p.recordCard(false);
        repo.saveProgress(p);

        Optional<DailyProgress> loaded =
                new JsonDailyGoalRepository(file.toString()).findProgress(LocalDate.of(2026, 5, 2));
        assertTrue(loaded.isPresent());
        assertEquals(2, loaded.get().getLearnedCards());
        assertEquals(1, loaded.get().getCorrectCards());
        assertEquals(10, loaded.get().getGoalCards());
    }

    @Test
    void findGoal_onMissingFile_returnsEmpty(@TempDir Path tmp) {
        Path file = tmp.resolve("does-not-exist.json");
        assertTrue(new JsonDailyGoalRepository(file.toString()).findGoal().isEmpty());
    }

    @Test
    void findProgress_onMissingDate_returnsEmpty(@TempDir Path tmp) {
        Path file = tmp.resolve("daily-goal.json");
        JsonDailyGoalRepository repo = new JsonDailyGoalRepository(file.toString());
        repo.saveGoal(new DailyGoal(5));
        assertTrue(repo.findProgress(LocalDate.of(2099, 1, 1)).isEmpty());
    }
}

