package de.sagaweschaefer.flashcard.infrastructure.persistence;

import de.sagaweschaefer.flashcard.application.port.LearningSessionResultRepository;
import de.sagaweschaefer.flashcard.model.SessionResult;
import de.sagaweschaefer.flashcard.util.JsonStorage;

import java.util.List;

public class JsonLearningSessionResultRepository implements LearningSessionResultRepository {
    private final JsonStorage storage;

    public JsonLearningSessionResultRepository(JsonStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<SessionResult> findAll() {
        return storage.loadSessionResults();
    }

    @Override
    public void saveAll(List<SessionResult> results) {
        storage.saveSessionResults(results);
    }
}

