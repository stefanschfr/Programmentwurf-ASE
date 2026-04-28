package de.sagaweschaefer.flashcard.application.session;

import de.sagaweschaefer.flashcard.model.Flashcard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PreparedSession {
    private final SessionMode mode;
    private final String sessionName;
    private final List<Flashcard> flashcards;
    private final long timeLimitMillis;

    public PreparedSession(SessionMode mode, String sessionName, List<Flashcard> flashcards, long timeLimitMillis) {
        this.mode = mode;
        this.sessionName = sessionName;
        this.flashcards = Collections.unmodifiableList(new ArrayList<>(flashcards));
        this.timeLimitMillis = timeLimitMillis;
    }

    public SessionMode getMode() {
        return mode;
    }

    public String getSessionName() {
        return sessionName;
    }

    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    public long getTimeLimitMillis() {
        return timeLimitMillis;
    }

    public boolean isExamMode() {
        return mode == SessionMode.EXAM;
    }
}

