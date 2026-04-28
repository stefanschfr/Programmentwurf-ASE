package de.sagaweschaefer.flashcard.application.port;

import de.sagaweschaefer.flashcard.application.session.PreparedSession;
import de.sagaweschaefer.flashcard.application.session.SessionSummary;
import de.sagaweschaefer.flashcard.model.Flashcard;

public interface SessionInteraction {
    void showSessionStarted(PreparedSession preparedSession);

    void showExamProgress(int currentQuestion, int totalQuestions, long remainingTimeMillis);

    boolean askQuestion(Flashcard flashcard);

    int requestSelfAssessment(boolean wasDue);

    void showCorrectAnswerFeedback();

    void showWrongAnswerFeedback(Flashcard flashcard);

    void showTimeExpired();

    void showSessionFinished(SessionSummary sessionSummary);
}

