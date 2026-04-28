package de.sagaweschaefer.flashcard.support;
import de.sagaweschaefer.flashcard.application.port.SessionInteraction;
import de.sagaweschaefer.flashcard.application.session.PreparedSession;
import de.sagaweschaefer.flashcard.application.session.SessionSummary;
import de.sagaweschaefer.flashcard.model.Flashcard;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
public class RecordingSessionInteraction implements SessionInteraction {
    private final Queue<Boolean> answers = new ArrayDeque<>();
    private final Queue<Integer> ratings = new ArrayDeque<>();
    private final List<String> events = new ArrayList<>();
    private int ratingRequests;
    private int askedQuestions;
    public void addAnswer(boolean answer) {
        answers.add(answer);
    }
    public void addRating(int rating) {
        ratings.add(rating);
    }
    public List<String> getEvents() {
        return events;
    }
    public int getRatingRequests() {
        return ratingRequests;
    }
    public int getAskedQuestions() {
        return askedQuestions;
    }
    @Override
    public void showSessionStarted(PreparedSession preparedSession) {
        events.add("started");
    }
    @Override
    public void showExamProgress(int currentQuestion, int totalQuestions, long remainingTimeMillis) {
        events.add("exam-progress-" + currentQuestion);
    }
    @Override
    public boolean askQuestion(Flashcard flashcard) {
        askedQuestions++;
        events.add("asked-" + flashcard.getQuestion());
        return !answers.isEmpty() && answers.remove();
    }
    @Override
    public int requestSelfAssessment(boolean wasDue) {
        ratingRequests++;
        events.add("rating-" + wasDue);
        return ratings.isEmpty() ? 2 : ratings.remove();
    }
    @Override
    public void showCorrectAnswerFeedback() {
        events.add("correct");
    }
    @Override
    public void showWrongAnswerFeedback(Flashcard flashcard) {
        events.add("wrong-" + flashcard.getQuestion());
    }
    @Override
    public void showTimeExpired() {
        events.add("time-expired");
    }
    @Override
    public void showSessionFinished(SessionSummary sessionSummary) {
        events.add("finished");
    }
}
