package de.sagaweschaefer.flashcard.testdoubles;

import de.sagaweschaefer.flashcard.domain.repository.DailyGoalRepository;
import de.sagaweschaefer.flashcard.domain.valueobject.DailyGoal;
import de.sagaweschaefer.flashcard.model.DailyProgress;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeDailyGoalRepository implements DailyGoalRepository {

    private DailyGoal goal;
    private final Map<LocalDate, DailyProgress> progressByDate = new HashMap<>();

    @Override
    public Optional<DailyGoal> findGoal() {
        return Optional.ofNullable(goal);
    }

    @Override
    public void saveGoal(DailyGoal goal) {
        this.goal = goal;
    }

    @Override
    public Optional<DailyProgress> findProgress(LocalDate date) {
        return Optional.ofNullable(progressByDate.get(date));
    }

    @Override
    public void saveProgress(DailyProgress progress) {
        progressByDate.put(progress.getDate(), progress);
    }

    public int progressEntryCount() {
        return progressByDate.size();
    }
}

