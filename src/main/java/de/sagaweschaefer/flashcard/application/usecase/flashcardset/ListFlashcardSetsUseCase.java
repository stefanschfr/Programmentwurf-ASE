package de.sagaweschaefer.flashcard.application.usecase.flashcardset;

import de.sagaweschaefer.flashcard.application.port.FlashcardSetRepository;
import de.sagaweschaefer.flashcard.model.FlashcardSet;

import java.util.ArrayList;
import java.util.List;

public class ListFlashcardSetsUseCase {
    private final FlashcardSetRepository flashcardSetRepository;

    public ListFlashcardSetsUseCase(FlashcardSetRepository flashcardSetRepository) {
        this.flashcardSetRepository = flashcardSetRepository;
    }

    public List<FlashcardSet> execute() {
        return new ArrayList<>(flashcardSetRepository.findAll());
    }
}

