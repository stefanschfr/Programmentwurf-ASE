package de.sagaweschaefer.flashcard.application.usecase;

import de.sagaweschaefer.flashcard.domain.service.DailyLearningPlanService;
import de.sagaweschaefer.flashcard.domain.valueobject.DailyGoal;
import de.sagaweschaefer.flashcard.model.DailyProgress;

@SuppressWarnings("unused")
public class GetDailyProgressUseCase {

    private final DailyLearningPlanService service;

    public GetDailyProgressUseCase(DailyLearningPlanService service) {
        this.service = service;
    }

    public Result execute() {
        DailyGoal goal = service.getGoalOrDefault();
        DailyProgress progress = service.getOrCreateTodayProgress();
        return new Result(goal, progress);
    }

    public static final class Result {
        private final DailyGoal goal;
        private final DailyProgress progress;

        public Result(DailyGoal goal, DailyProgress progress) {
            this.goal = goal;
            this.progress = progress;
        }

        public DailyGoal getGoal() {
            return goal;
        }

        public DailyProgress getProgress() {
            return progress;
        }
    }
}


