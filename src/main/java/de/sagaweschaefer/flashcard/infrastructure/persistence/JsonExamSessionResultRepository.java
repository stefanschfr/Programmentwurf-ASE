package de.sagaweschaefer.flashcard.infrastructure.persistence;

import de.sagaweschaefer.flashcard.application.port.ExamSessionResultRepository;
import de.sagaweschaefer.flashcard.model.SessionResult;
import de.sagaweschaefer.flashcard.util.JsonStorage;

import java.util.List;

public class JsonExamSessionResultRepository implements ExamSessionResultRepository {
    private final JsonStorage storage;

    public JsonExamSessionResultRepository(JsonStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<SessionResult> findAll() {
        return storage.loadExamResults();
    }

    @Override
    public void saveAll(List<SessionResult> results) {
        storage.saveExamResults(results);
    }
}

