package de.sagaweschaefer.flashcard.application.usecase;

import de.sagaweschaefer.flashcard.domain.repository.FlashcardSetRepository;
import de.sagaweschaefer.flashcard.model.FlashcardSet;

import java.util.List;

public class CreateFlashcardSetUseCase {
    private final FlashcardSetRepository repository;

    public CreateFlashcardSetUseCase(FlashcardSetRepository repository) {
        this.repository = repository;
    }

    public FlashcardSet execute(String name) {
        List<FlashcardSet> sets = repository.findAll();
        FlashcardSet newSet = new FlashcardSet(name);
        sets.add(newSet);
        repository.saveAll(sets);
        return newSet;
    }
}

