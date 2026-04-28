package de.sagaweschaefer.flashcard.application.usecase.flashcardset;

import de.sagaweschaefer.flashcard.application.port.FlashcardSetRepository;
import de.sagaweschaefer.flashcard.application.port.FlashcardStatisticsRepository;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DeleteFlashcardSetUseCase {
    private final FlashcardSetRepository flashcardSetRepository;
    private final FlashcardStatisticsRepository flashcardStatisticsRepository;

    public DeleteFlashcardSetUseCase(FlashcardSetRepository flashcardSetRepository,
                                     FlashcardStatisticsRepository flashcardStatisticsRepository) {
        this.flashcardSetRepository = flashcardSetRepository;
        this.flashcardStatisticsRepository = flashcardStatisticsRepository;
    }

    public Optional<FlashcardSet> execute(FlashcardSet selectedSet) {
        List<FlashcardSet> flashcardSets = flashcardSetRepository.findAll();
        int index = findSetIndex(flashcardSets, selectedSet);
        if (index < 0) {
            return Optional.empty();
        }

        FlashcardSet removedSet = flashcardSets.remove(index);
        cleanupStatistics(removedSet);
        flashcardSetRepository.saveAll(flashcardSets);
        return Optional.of(removedSet);
    }

    private void cleanupStatistics(FlashcardSet removedSet) {
        Map<String, FlashcardStatistics> statisticsMap = flashcardStatisticsRepository.findAll();
        boolean changed = false;

        for (Flashcard flashcard : removedSet.getFlashcards()) {
            if (statisticsMap.remove(flashcard.getId()) != null) {
                changed = true;
            }
        }

        if (changed) {
            flashcardStatisticsRepository.saveAll(statisticsMap);
        }
    }

    private int findSetIndex(List<FlashcardSet> flashcardSets, FlashcardSet selectedSet) {
        for (int i = 0; i < flashcardSets.size(); i++) {
            if (isSameSet(flashcardSets.get(i), selectedSet)) {
                return i;
            }
        }
        return -1;
    }

    private boolean isSameSet(FlashcardSet first, FlashcardSet second) {
        return first == second
                || (first != null
                && second != null
                && first.getSetName() != null
                && first.getSetName().equals(second.getSetName()));
    }
}

