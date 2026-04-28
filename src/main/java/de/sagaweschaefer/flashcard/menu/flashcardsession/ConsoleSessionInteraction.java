package de.sagaweschaefer.flashcard.menu.flashcardsession;

import de.sagaweschaefer.flashcard.application.port.SessionInteraction;
import de.sagaweschaefer.flashcard.application.session.PreparedSession;
import de.sagaweschaefer.flashcard.application.session.SessionSummary;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.util.MenuUtils;

public class ConsoleSessionInteraction implements SessionInteraction {
    @Override
    public void showSessionStarted(PreparedSession preparedSession) {
        if (preparedSession.isExamMode()) {
            System.out.println("\n--- Prüfung gestartet: " + preparedSession.getSessionName() + " ---");
            System.out.println("Anzahl Fragen: " + preparedSession.getFlashcards().size());
            System.out.println("Zeitlimit: " + FlashcardSessionStatistics.formatTime(preparedSession.getTimeLimitMillis()));
            return;
        }

        System.out.println("\n--- Session gestartet: " + preparedSession.getSessionName() + " ---");
    }

    @Override
    public void showExamProgress(int currentQuestion, int totalQuestions, long remainingTimeMillis) {
        if (totalQuestions <= 0) {
            return;
        }
        System.out.println("\nFrage " + currentQuestion + " von " + totalQuestions);
        System.out.println("Verbleibende Zeit: " + FlashcardSessionStatistics.formatTime(remainingTimeMillis));
    }

    @Override
    public boolean askQuestion(Flashcard flashcard) {
        return FlashcardQuestionHelper.askQuestion(flashcard);
    }

    @Override
    public int requestSelfAssessment(boolean wasDue) {
        System.out.println("Wie gut konntest du die Frage beantworten?");
        System.out.println("1. Nicht so gut (Level -1)");
        System.out.println("2. Ganz ok (Level bleibt)");
        if (wasDue) {
            System.out.println("3. Sehr gut (Level +1)");
        }
        return MenuUtils.promptForInt("Deine Wahl: ");
    }

    @Override
    public void showCorrectAnswerFeedback() {
        System.out.println("Richtig!");
    }

    @Override
    public void showWrongAnswerFeedback(Flashcard flashcard) {
        System.out.println("Falsch! Die richtige Antwort war: " + flashcard.getCorrectAnswerDisplay());
    }

    @Override
    public void showTimeExpired() {
        System.out.println("\n!!! ZEIT ABGELAUFEN !!!");
    }

    @Override
    public void showSessionFinished(SessionSummary sessionSummary) {
        if (sessionSummary.getMode() == de.sagaweschaefer.flashcard.application.session.SessionMode.EXAM) {
            System.out.println("\n--- Prüfung beendet ---");
        }
        FlashcardSessionStatistics.displaySessionResult(
                sessionSummary.getCorrectCount(),
                sessionSummary.getTotalCount(),
                sessionSummary.getDurationMillis()
        );
    }
}

