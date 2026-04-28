package de.sagaweschaefer.flashcard.support;

import de.sagaweschaefer.flashcard.application.port.FlashcardSetRepository;
import de.sagaweschaefer.flashcard.model.FlashcardSet;

import java.util.ArrayList;
import java.util.List;

public class InMemoryFlashcardSetRepository implements FlashcardSetRepository {
    private List<FlashcardSet> flashcardSets;

    public InMemoryFlashcardSetRepository() {
        this(new ArrayList<>());
    }

    public InMemoryFlashcardSetRepository(List<FlashcardSet> flashcardSets) {
        this.flashcardSets = new ArrayList<>(flashcardSets);
    }

    @Override
    public List<FlashcardSet> findAll() {
        return flashcardSets;
    }

    @Override
    public void saveAll(List<FlashcardSet> flashcardSets) {
        this.flashcardSets = flashcardSets;
    }
}

