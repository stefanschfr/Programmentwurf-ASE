package de.sagaweschaefer.flashcard.application.usecase;

import de.sagaweschaefer.flashcard.domain.repository.FlashcardSetRepository;
import de.sagaweschaefer.flashcard.domain.repository.StatisticsRepository;
import de.sagaweschaefer.flashcard.domain.service.DailyLearningPlanService;
import de.sagaweschaefer.flashcard.model.Flashcard;

import java.util.List;

/**
 * UseCase: Karten für die heutige Lerneinheit empfehlen.
 *
 * <p>Lädt Sets und Statistiken über die Repositories und delegiert die
 * Auswahl-Logik an den Domain-Service. Vermeidet Doppelarbeit (DRY) für
 * Aufrufer im Adapter-Layer.</p>
 */
@SuppressWarnings("unused") // Wird via ApplicationContext im DailyPlanMenu verwendet
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


