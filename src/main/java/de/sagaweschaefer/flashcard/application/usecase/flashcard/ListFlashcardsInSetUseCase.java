package de.sagaweschaefer.flashcard.application.usecase.flashcard;

import de.sagaweschaefer.flashcard.application.port.FlashcardSetRepository;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;

import java.util.ArrayList;
import java.util.List;

public class ListFlashcardsInSetUseCase {
    private final FlashcardSetRepository flashcardSetRepository;

    public ListFlashcardsInSetUseCase(FlashcardSetRepository flashcardSetRepository) {
        this.flashcardSetRepository = flashcardSetRepository;
    }

    public List<Flashcard> execute(FlashcardSet selectedSet) {
        return flashcardSetRepository.findAll().stream()
                .filter(set -> isSameSet(set, selectedSet))
                .findFirst()
                .map(set -> new ArrayList<>(set.getFlashcards()))
                .orElseGet(ArrayList::new);
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

