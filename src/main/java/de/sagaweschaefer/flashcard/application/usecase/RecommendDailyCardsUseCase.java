package de.sagaweschaefer.flashcard.application.usecase;

import de.sagaweschaefer.flashcard.domain.repository.FlashcardSetRepository;
import de.sagaweschaefer.flashcard.domain.repository.StatisticsRepository;
import de.sagaweschaefer.flashcard.domain.service.DailyLearningPlanService;
import de.sagaweschaefer.flashcard.model.Flashcard;

import java.util.List;

@SuppressWarnings("unused")
public class RecommendDailyCardsUseCase {

    private final FlashcardSetRepository setRepository;
    private final StatisticsRepository statisticsRepository;
    private final DailyLearningPlanService service;

    public RecommendDailyCardsUseCase(FlashcardSetRepository setRepository,
                                      StatisticsRepository statisticsRepository,
                                      DailyLearningPlanService service) {
        this.setRepository = setRepository;
        this.statisticsRepository = statisticsRepository;
        this.service = service;
    }

    public List<Flashcard> execute() {
        return service.recommendCardsForToday(
                setRepository.findAll(),
                statisticsRepository.findAll()
        );
    }
}


