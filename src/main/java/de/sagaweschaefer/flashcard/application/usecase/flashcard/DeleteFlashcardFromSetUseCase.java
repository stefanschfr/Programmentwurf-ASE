package de.sagaweschaefer.flashcard.application.usecase.flashcard;

import de.sagaweschaefer.flashcard.application.port.FlashcardSetRepository;
import de.sagaweschaefer.flashcard.application.port.FlashcardStatisticsRepository;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DeleteFlashcardFromSetUseCase {
    private final FlashcardSetRepository flashcardSetRepository;
    private final FlashcardStatisticsRepository flashcardStatisticsRepository;

    public DeleteFlashcardFromSetUseCase(FlashcardSetRepository flashcardSetRepository,
                                         FlashcardStatisticsRepository flashcardStatisticsRepository) {
        this.flashcardSetRepository = flashcardSetRepository;
        this.flashcardStatisticsRepository = flashcardStatisticsRepository;
    }

    public Optional<Flashcard> execute(FlashcardSet selectedSet, int flashcardIndex) {
        List<FlashcardSet> flashcardSets = flashcardSetRepository.findAll();
        FlashcardSet managedSet = findManagedSet(flashcardSets, selectedSet);
        if (managedSet == null) {
            return Optional.empty();
        }
        if (flashcardIndex < 0 || flashcardIndex >= managedSet.getFlashcards().size()) {
            return Optional.empty();
        }

        Flashcard removedFlashcard = managedSet.getFlashcards().remove(flashcardIndex);
        if (selectedSet != null && managedSet != selectedSet) {
            selectedSet.setFlashcards(new ArrayList<>(managedSet.getFlashcards()));
        }
        flashcardSetRepository.saveAll(flashcardSets);
        cleanupStatistics(removedFlashcard);
        return Optional.of(removedFlashcard);
    }

    private FlashcardSet findManagedSet(List<FlashcardSet> flashcardSets, FlashcardSet selectedSet) {
        for (FlashcardSet flashcardSet : flashcardSets) {
            if (isSameSet(flashcardSet, selectedSet)) {
                return flashcardSet;
            }
        }
        return null;
    }

    private void cleanupStatistics(Flashcard removedFlashcard) {
        Map<String, FlashcardStatistics> statisticsMap = flashcardStatisticsRepository.findAll();
        if (statisticsMap.remove(removedFlashcard.getId()) != null) {
            flashcardStatisticsRepository.saveAll(statisticsMap);
        }
    }

    private boolean isSameSet(FlashcardSet first, FlashcardSet second) {
        if (first == second) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        if (first.getName() == null || second.getName() == null) {
            return false;
        }
        return first.getName().equalsIgnoreCase(second.getName());
    }
}


