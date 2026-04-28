package de.sagaweschaefer.flashcard.application.port;

import de.sagaweschaefer.flashcard.model.SessionResult;

import java.util.List;

public interface ExamSessionResultRepository {
    List<SessionResult> findAll();

    void saveAll(List<SessionResult> results);
}

