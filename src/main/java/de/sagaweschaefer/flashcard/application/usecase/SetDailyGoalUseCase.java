package de.sagaweschaefer.flashcard.application.usecase;

import de.sagaweschaefer.flashcard.domain.service.DailyLearningPlanService;
import de.sagaweschaefer.flashcard.domain.valueobject.DailyGoal;

@SuppressWarnings("unused")
public class SetDailyGoalUseCase {

    private final DailyLearningPlanService service;

    public SetDailyGoalUseCase(DailyLearningPlanService service) {
        this.service = service;
    }

    public DailyGoal execute(int cardsPerDay) {
        DailyGoal goal = new DailyGoal(cardsPerDay);
        service.setGoal(goal);
        return goal;
    }
}


