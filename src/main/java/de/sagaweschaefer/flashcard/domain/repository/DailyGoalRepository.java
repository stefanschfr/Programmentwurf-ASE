package de.sagaweschaefer.flashcard.domain.repository;

import de.sagaweschaefer.flashcard.domain.valueobject.DailyGoal;
import de.sagaweschaefer.flashcard.model.DailyProgress;

import java.time.LocalDate;
import java.util.Optional;

@SuppressWarnings("unused")
public interface DailyGoalRepository {

    Optional<DailyGoal> findGoal();

    void saveGoal(DailyGoal goal);

    Optional<DailyProgress> findProgress(LocalDate date);

    void saveProgress(DailyProgress progress);
}


