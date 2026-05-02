package de.sagaweschaefer.flashcard.application.usecase;

import de.sagaweschaefer.flashcard.domain.repository.FlashcardSetRepository;
import de.sagaweschaefer.flashcard.domain.repository.StatisticsRepository;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardSetStatistics;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;
import de.sagaweschaefer.flashcard.model.GeneralStatistics;

import java.util.List;
import java.util.Map;

/**
 * UseCase: Statistiken abrufen.
 *
 * <p>Liefert entweder die Gesamtstatistik oder die Statistik für ein einzelnes Set.
 * Hängt nur von Domain-Interfaces ab (DIP, Dependency Rule). Berechnung wird an
 * die bestehenden Statistik-Models delegiert.</p>
 */
public class GetStatisticsUseCase {

    private final FlashcardSetRepository setRepository;
    private final StatisticsRepository statisticsRepository;

    public GetStatisticsUseCase(FlashcardSetRepository setRepository,
                                StatisticsRepository statisticsRepository) {
        this.setRepository = setRepository;
        this.statisticsRepository = statisticsRepository;
    }

    /**
     * Berechnet die Gesamtstatistik über alle Sets.
     */
    public GeneralStatistics getGeneralStatistics() {
        List<FlashcardSet> sets = setRepository.findAll();
        Map<String, FlashcardStatistics> stats = statisticsRepository.findAll();
        return GeneralStatistics.calculate(sets, stats);
    }

    /**
     * Berechnet die Statistik für ein einzelnes Set.
     *
     * @param setIndex 0-basierter Index
     * @return Set-Statistik
     * @throws IndexOutOfBoundsException bei ungültigem Index
     */
    public FlashcardSetStatistics getSetStatistics(int setIndex) {
        List<FlashcardSet> sets = setRepository.findAll();
        if (setIndex < 0 || setIndex >= sets.size()) {
            throw new IndexOutOfBoundsException(
                    "setIndex " + setIndex + " außerhalb des Bereichs (0.." + (sets.size() - 1) + ")");
        }
        Map<String, FlashcardStatistics> stats = statisticsRepository.findAll();
        return FlashcardSetStatistics.calculate(sets.get(setIndex), stats);
    }
}

