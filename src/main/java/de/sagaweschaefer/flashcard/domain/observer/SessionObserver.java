package de.sagaweschaefer.flashcard.domain.observer;

import de.sagaweschaefer.flashcard.model.Flashcard;

public interface SessionObserver {
    void onQuestionAnswered(Flashcard card, boolean correct, int rating, boolean wasDue);
    void onSessionFinished(String sessionName, int correctCount, int totalCount, long durationMillis);
}

