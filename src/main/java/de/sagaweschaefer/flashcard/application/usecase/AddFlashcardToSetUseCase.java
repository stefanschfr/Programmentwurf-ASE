package de.sagaweschaefer.flashcard.application.usecase;

import de.sagaweschaefer.flashcard.domain.repository.FlashcardSetRepository;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;

import java.util.List;

public class AddFlashcardToSetUseCase {
    private final FlashcardSetRepository repository;

    public AddFlashcardToSetUseCase(FlashcardSetRepository repository) {
        this.repository = repository;
    }

    public void execute(String setName, Flashcard flashcard) {
        List<FlashcardSet> sets = repository.findAll();
        for (FlashcardSet set : sets) {
            if (set.getName().equals(setName)) {
                set.getFlashcards().add(flashcard);
                repository.saveAll(sets);
                return;
            }
        }
        throw new IllegalArgumentException("Set '" + setName + "' nicht gefunden.");
    }
}

