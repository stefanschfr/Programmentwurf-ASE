package de.sagaweschaefer.flashcard.support;

import de.sagaweschaefer.flashcard.application.port.LearningSessionResultRepository;
import de.sagaweschaefer.flashcard.model.SessionResult;

import java.util.ArrayList;
import java.util.List;

public class InMemoryLearningSessionResultRepository implements LearningSessionResultRepository {
    private List<SessionResult> results = new ArrayList<>();

    @Override
    public List<SessionResult> findAll() {
        return results;
    }

    @Override
    public void saveAll(List<SessionResult> results) {
        this.results = results;
    }
}

