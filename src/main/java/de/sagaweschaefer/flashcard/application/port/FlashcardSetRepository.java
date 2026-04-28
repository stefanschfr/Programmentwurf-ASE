package de.sagaweschaefer.flashcard.application.port;

import de.sagaweschaefer.flashcard.model.FlashcardSet;

import java.util.List;

public interface FlashcardSetRepository {
    List<FlashcardSet> findAll();

    void saveAll(List<FlashcardSet> flashcardSets);
}

