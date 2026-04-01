package de.sagaweschaefer.flashcard.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FlashcardStatistics implements Serializable {
    private static final long serialVersionUID = 1L;

    private String flashcardId;
    private LocalDateTime lastCorrectAt;
    private int correctCount;
    private int wrongCount;

    public FlashcardStatistics() {}

    public FlashcardStatistics(String flashcardId) {
        this.flashcardId = flashcardId;
        this.correctCount = 0;
        this.wrongCount = 0;
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

    public void incrementCorrect() {
        this.correctCount++;
        this.lastCorrectAt = LocalDateTime.now();
    }

    public void incrementWrong() {
        this.wrongCount++;
    }
}
