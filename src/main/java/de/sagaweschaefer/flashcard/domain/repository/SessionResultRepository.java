package de.sagaweschaefer.flashcard.domain.repository;

import de.sagaweschaefer.flashcard.model.SessionResult;

import java.util.List;

public interface SessionResultRepository {
    List<SessionResult> findAll();
    void saveAll(List<SessionResult> results);
}

