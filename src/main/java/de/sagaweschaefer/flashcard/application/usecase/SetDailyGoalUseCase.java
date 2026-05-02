package de.sagaweschaefer.flashcard.application.usecase;

import de.sagaweschaefer.flashcard.domain.service.DailyLearningPlanService;
import de.sagaweschaefer.flashcard.domain.valueobject.DailyGoal;

/**
 * UseCase: Tagesziel setzen.
 *
 * <p>Hängt nur vom Domain-Service ab (Dependency Rule, DIP).</p>
 */
@SuppressWarnings("unused") // Wird via ApplicationContext im DailyPlanMenu verwendet
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


