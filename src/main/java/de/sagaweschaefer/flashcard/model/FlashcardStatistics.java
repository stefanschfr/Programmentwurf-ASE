package de.sagaweschaefer.flashcard.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlashcardStatistics implements Serializable {
    private static final long serialVersionUID = 1L;

    private String flashcardId;
    private LocalDateTime lastCorrectAt;
    private int correctCount;
    private int wrongCount;
    private int level;

    public FlashcardStatistics() {}

    public FlashcardStatistics(String flashcardId) {
        this.flashcardId = flashcardId;
        this.correctCount = 0;
        this.wrongCount = 0;
        this.level = 0;
    }

    public String getFlashcardId() {
        return flashcardId;
    }

    public void setFlashcardId(String flashcardId) {
        this.flashcardId = flashcardId;
    }

    public LocalDateTime getLastCorrectAt() {
        return lastCorrectAt;
    }

    public void setLastCorrectAt(LocalDateTime lastCorrectAt) {
        this.lastCorrectAt = lastCorrectAt;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

    public int getWrongCount() {
        return wrongCount;
    }

    public void setWrongCount(int wrongCount) {
        this.wrongCount = wrongCount;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void incrementCorrect() {
        this.correctCount++;
        this.lastCorrectAt = LocalDateTime.now();
        if (this.level < 6) {
            this.level++;
        }
    }

    public void incrementWrong() {
        this.wrongCount++;
        this.level = 0;
    }

    @JsonIgnore
    public boolean isDue() {
        if (level == 0 || lastCorrectAt == null) {
            return true;
        }

        LocalDateTime now = LocalDateTime.now();
        switch (level) {
            case 1:
                return lastCorrectAt.plusMinutes(1).isBefore(now);
            case 2:
                return lastCorrectAt.plusMinutes(10).isBefore(now);
            case 3:
                return lastCorrectAt.plusHours(5).isBefore(now);
            case 4:
                return lastCorrectAt.plusDays(1).isBefore(now);
            case 5:
                return lastCorrectAt.plusDays(14).isBefore(now);
            case 6:
                return lastCorrectAt.plusMonths(1).isBefore(now);
            default:
                return true;
        }
    }
}
