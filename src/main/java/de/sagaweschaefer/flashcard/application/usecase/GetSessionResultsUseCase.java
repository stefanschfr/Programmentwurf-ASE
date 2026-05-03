package de.sagaweschaefer.flashcard.application.usecase;

import de.sagaweschaefer.flashcard.domain.repository.SessionResultRepository;
import de.sagaweschaefer.flashcard.model.SessionResult;

import java.util.Collections;
import java.util.List;

public class GetSessionResultsUseCase {

    private final SessionResultRepository repository;

    public GetSessionResultsUseCase(SessionResultRepository repository) {
        this.repository = repository;
    }

    public List<SessionResult> execute(int limit) {
        List<SessionResult> all = repository.findAll();
        if (all.isEmpty()) {
            return List.of();
        }
        int size = all.size();
        int count = (limit > 0) ? Math.min(limit, size) : size;
        List<SessionResult> result = new java.util.ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            result.add(all.get(size - 1 - i));
        }
        return Collections.unmodifiableList(result);
    }
}

