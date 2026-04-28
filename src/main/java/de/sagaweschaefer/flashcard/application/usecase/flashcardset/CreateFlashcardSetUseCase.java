package de.sagaweschaefer.flashcard.application.usecase.flashcardset;

import de.sagaweschaefer.flashcard.application.port.FlashcardSetRepository;
import de.sagaweschaefer.flashcard.model.FlashcardSet;

import java.util.List;

public class CreateFlashcardSetUseCase {
    private final FlashcardSetRepository flashcardSetRepository;

    public CreateFlashcardSetUseCase(FlashcardSetRepository flashcardSetRepository) {
        this.flashcardSetRepository = flashcardSetRepository;
    }

    public FlashcardSet execute(String setName) {
        String normalizedName = normalizeName(setName);
        List<FlashcardSet> flashcardSets = flashcardSetRepository.findAll();

        boolean alreadyExists = flashcardSets.stream()
                .map(FlashcardSet::getName)
                .filter(name -> name != null && !name.isBlank())
                .anyMatch(name -> name.equalsIgnoreCase(normalizedName));

        if (alreadyExists) {
            throw new IllegalArgumentException("Ein Lernkartenset mit diesem Namen existiert bereits.");
        }

        FlashcardSet flashcardSet = new FlashcardSet(normalizedName);
        flashcardSets.add(flashcardSet);
        flashcardSetRepository.saveAll(flashcardSets);
        return flashcardSet;
    }

    private String normalizeName(String setName) {
        if (setName == null || setName.trim().isEmpty()) {
            throw new IllegalArgumentException("Der Name des Lernkartensets darf nicht leer sein.");
        }
        return setName.trim();
    }
}

