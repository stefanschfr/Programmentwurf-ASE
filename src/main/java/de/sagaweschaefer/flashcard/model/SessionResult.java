package de.sagaweschaefer.flashcard.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

public class SessionResult {
    private String sessionName;
    private int correctCount;
    private int totalCount;
    private long durationMillis;
    private LocalDateTime timestamp;

    public SessionResult() {
    }

    public SessionResult(String sessionName, int correctCount, int totalCount, long durationMillis) {
        this.sessionName = sessionName;
        this.correctCount = correctCount;
        this.totalCount = totalCount;
        this.durationMillis = durationMillis;
        this.timestamp = LocalDateTime.now();
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public long getDurationMillis() {
        return durationMillis;
    }

    public void setDurationMillis(long durationMillis) {
        this.durationMillis = durationMillis;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @JsonIgnore
    public double getPercentage() {
        return totalCount > 0 ? (double) correctCount / totalCount * 100 : 0;
    }
}
