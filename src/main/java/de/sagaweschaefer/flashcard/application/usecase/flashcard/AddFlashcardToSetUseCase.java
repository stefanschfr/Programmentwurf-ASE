package de.sagaweschaefer.flashcard.application.usecase.flashcard;

import de.sagaweschaefer.flashcard.application.port.FlashcardSetRepository;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;

import java.util.ArrayList;
import java.util.List;

public class AddFlashcardToSetUseCase {
    private final FlashcardSetRepository flashcardSetRepository;

    public AddFlashcardToSetUseCase(FlashcardSetRepository flashcardSetRepository) {
        this.flashcardSetRepository = flashcardSetRepository;
    }

    public Flashcard execute(FlashcardSet selectedSet, Flashcard flashcard) {
        validateFlashcard(flashcard);
        List<FlashcardSet> flashcardSets = flashcardSetRepository.findAll();
        FlashcardSet managedSet = findManagedSet(flashcardSets, selectedSet);
        managedSet.getFlashcards().add(flashcard);
        if (selectedSet != null && managedSet != selectedSet) {
            selectedSet.setFlashcards(new ArrayList<>(managedSet.getFlashcards()));
        }
        flashcardSetRepository.saveAll(flashcardSets);
        return flashcard;
    }

    private void validateFlashcard(Flashcard flashcard) {
        if (flashcard == null) {
            throw new IllegalArgumentException("Es wurde keine Lernkarte übergeben.");
        }
        if (flashcard.getQuestion() == null || flashcard.getQuestion().trim().isEmpty()) {
            throw new IllegalArgumentException("Die Frage der Lernkarte darf nicht leer sein.");
        }
    }

    private FlashcardSet findManagedSet(List<FlashcardSet> flashcardSets, FlashcardSet selectedSet) {
        return flashcardSets.stream()
                .filter(set -> isSameSet(set, selectedSet))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Das ausgewählte Lernkartenset wurde nicht gefunden."));
    }

    private boolean isSameSet(FlashcardSet first, FlashcardSet second) {
        return first == second
                || (first != null
                && second != null
                && first.getSetName() != null
                && first.getSetName().equals(second.getSetName()));
    }
}


