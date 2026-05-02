package de.sagaweschaefer.flashcard.application;

import de.sagaweschaefer.flashcard.adapter.persistence.JsonDailyGoalRepository;
import de.sagaweschaefer.flashcard.adapter.persistence.JsonFlashcardSetRepository;
import de.sagaweschaefer.flashcard.adapter.persistence.JsonSessionResultRepository;
import de.sagaweschaefer.flashcard.adapter.persistence.JsonStatisticsRepository;
import de.sagaweschaefer.flashcard.application.usecase.DeleteFlashcardSetUseCase;
import de.sagaweschaefer.flashcard.application.usecase.GetDailyProgressUseCase;
import de.sagaweschaefer.flashcard.application.usecase.GetSessionResultsUseCase;
import de.sagaweschaefer.flashcard.application.usecase.GetStatisticsUseCase;
import de.sagaweschaefer.flashcard.application.usecase.RecommendDailyCardsUseCase;
import de.sagaweschaefer.flashcard.application.usecase.SetDailyGoalUseCase;
import de.sagaweschaefer.flashcard.domain.repository.DailyGoalRepository;
import de.sagaweschaefer.flashcard.domain.repository.FlashcardSetRepository;
import de.sagaweschaefer.flashcard.domain.repository.SessionResultRepository;
import de.sagaweschaefer.flashcard.domain.repository.StatisticsRepository;
import de.sagaweschaefer.flashcard.domain.service.DailyLearningPlanService;

public class ApplicationContext {
    private static final String FLASHCARD_SETS_PATH = "data/flashcard-sets.json";
    private static final String STATISTICS_PATH = "data/flashcard-statistics.json";
    private static final String SESSION_RESULTS_PATH = "data/session-results.json";
    private static final String EXAM_RESULTS_PATH = "data/exam-results.json";
    private static final String DAILY_GOAL_PATH = "data/daily-goal.json";

    private final FlashcardSetRepository flashcardSetRepository;
    private final StatisticsRepository statisticsRepository;
    private final SessionResultRepository sessionResultRepository;
    private final SessionResultRepository examResultRepository;

    private final DailyLearningPlanService dailyLearningPlanService;
    private final SetDailyGoalUseCase setDailyGoalUseCase;
    private final GetDailyProgressUseCase getDailyProgressUseCase;
    private final RecommendDailyCardsUseCase recommendDailyCardsUseCase;
    private final DeleteFlashcardSetUseCase deleteFlashcardSetUseCase;
    private final GetStatisticsUseCase getStatisticsUseCase;
    private final GetSessionResultsUseCase getSessionResultsUseCase;
    private final GetSessionResultsUseCase getExamResultsUseCase;

    public ApplicationContext() {
        this.flashcardSetRepository = new JsonFlashcardSetRepository(FLASHCARD_SETS_PATH);
        this.statisticsRepository = new JsonStatisticsRepository(STATISTICS_PATH);
        this.sessionResultRepository = new JsonSessionResultRepository(SESSION_RESULTS_PATH);
        this.examResultRepository = new JsonSessionResultRepository(EXAM_RESULTS_PATH);
        DailyGoalRepository dailyGoalRepository = new JsonDailyGoalRepository(DAILY_GOAL_PATH);

        this.dailyLearningPlanService = new DailyLearningPlanService(dailyGoalRepository);
        this.setDailyGoalUseCase = new SetDailyGoalUseCase(dailyLearningPlanService);
        this.getDailyProgressUseCase = new GetDailyProgressUseCase(dailyLearningPlanService);
        this.recommendDailyCardsUseCase = new RecommendDailyCardsUseCase(
                flashcardSetRepository, statisticsRepository, dailyLearningPlanService);
        this.deleteFlashcardSetUseCase = new DeleteFlashcardSetUseCase(flashcardSetRepository);
        this.getStatisticsUseCase = new GetStatisticsUseCase(flashcardSetRepository, statisticsRepository);
        this.getSessionResultsUseCase = new GetSessionResultsUseCase(sessionResultRepository);
        this.getExamResultsUseCase = new GetSessionResultsUseCase(examResultRepository);
    }

    public FlashcardSetRepository getFlashcardSetRepository() {
        return flashcardSetRepository;
    }

    public StatisticsRepository getStatisticsRepository() {
        return statisticsRepository;
    }

    public SessionResultRepository getSessionResultRepository() {
        return sessionResultRepository;
    }

    public SessionResultRepository getExamResultRepository() {
        return examResultRepository;
    }

    public DailyLearningPlanService getDailyLearningPlanService() {
        return dailyLearningPlanService;
    }

    public SetDailyGoalUseCase getSetDailyGoalUseCase() {
        return setDailyGoalUseCase;
    }

    public GetDailyProgressUseCase getGetDailyProgressUseCase() {
        return getDailyProgressUseCase;
    }

    public RecommendDailyCardsUseCase getRecommendDailyCardsUseCase() {
        return recommendDailyCardsUseCase;
    }

    @SuppressWarnings("unused") // Wird für schrittweise Menü-Migration über den ApplicationContext bereitgehalten
    public DeleteFlashcardSetUseCase getDeleteFlashcardSetUseCase() {
        return deleteFlashcardSetUseCase;
    }

    @SuppressWarnings("unused") // Wird für schrittweise Menü-Migration über den ApplicationContext bereitgehalten
    public GetStatisticsUseCase getGetStatisticsUseCase() {
        return getStatisticsUseCase;
    }

    @SuppressWarnings("unused") // Wird für schrittweise Menü-Migration über den ApplicationContext bereitgehalten
    public GetSessionResultsUseCase getGetSessionResultsUseCase() {
        return getSessionResultsUseCase;
    }

    @SuppressWarnings("unused") // Wird für schrittweise Menü-Migration über den ApplicationContext bereitgehalten
    public GetSessionResultsUseCase getGetExamResultsUseCase() {
        return getExamResultsUseCase;
    }
}
