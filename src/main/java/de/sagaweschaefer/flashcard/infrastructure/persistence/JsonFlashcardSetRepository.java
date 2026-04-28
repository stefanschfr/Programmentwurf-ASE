package de.sagaweschaefer.flashcard.infrastructure.persistence;

import de.sagaweschaefer.flashcard.application.port.FlashcardSetRepository;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.util.JsonStorage;

import java.util.List;

public class JsonFlashcardSetRepository implements FlashcardSetRepository {
    private final JsonStorage storage;

    public JsonFlashcardSetRepository(JsonStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<FlashcardSet> findAll() {
        return storage.loadFlashcardSets();
    }

    @Override
    public void saveAll(List<FlashcardSet> flashcardSets) {
        storage.saveFlashcardSets(flashcardSets);
    }
}

