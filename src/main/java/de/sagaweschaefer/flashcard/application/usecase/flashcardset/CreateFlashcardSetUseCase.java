package de.sagaweschaefer.flashcard.application.usecase.flashcardset;

import de.sagaweschaefer.flashcard.application.port.FlashcardSetRepository;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardSetName;

import java.util.List;

public class CreateFlashcardSetUseCase {
    private final FlashcardSetRepository flashcardSetRepository;

    public CreateFlashcardSetUseCase(FlashcardSetRepository flashcardSetRepository) {
        this.flashcardSetRepository = flashcardSetRepository;
    }

    public FlashcardSet execute(String setName) {
        FlashcardSetName normalizedName = FlashcardSetName.of(setName);
        List<FlashcardSet> flashcardSets = flashcardSetRepository.findAll();

        boolean alreadyExists = flashcardSets.stream()
                .anyMatch(set -> normalizedName.equals(set.getSetName()));

        if (alreadyExists) {
            throw new IllegalArgumentException("Ein Lernkartenset mit diesem Namen existiert bereits.");
        }

        FlashcardSet flashcardSet = new FlashcardSet(normalizedName);
        flashcardSets.add(flashcardSet);
        flashcardSetRepository.saveAll(flashcardSets);
        return flashcardSet;
    }
}

