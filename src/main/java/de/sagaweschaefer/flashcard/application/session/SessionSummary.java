package de.sagaweschaefer.flashcard.application.session;

public class SessionSummary {
    private final String sessionName;
    private final SessionMode mode;
    private final int correctCount;
    private final int totalCount;
    private final long durationMillis;
    private final boolean timedOut;

    public SessionSummary(String sessionName,
                          SessionMode mode,
                          int correctCount,
                          int totalCount,
                          long durationMillis,
                          boolean timedOut) {
        this.sessionName = sessionName;
        this.mode = mode;
        this.correctCount = correctCount;
        this.totalCount = totalCount;
        this.durationMillis = durationMillis;
        this.timedOut = timedOut;
    }

    public String getSessionName() {
        return sessionName;
    }

    public SessionMode getMode() {
        return mode;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public boolean isTimedOut() {
        return timedOut;
    }
}

