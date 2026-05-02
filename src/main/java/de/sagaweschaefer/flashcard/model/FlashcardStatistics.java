package de.sagaweschaefer.flashcard.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.sagaweschaefer.flashcard.domain.service.SpacedRepetitionService;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused") // Setter werden via Reflection durch Jackson aufgerufen
public class FlashcardStatistics implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private static final SpacedRepetitionService SPACED_REPETITION = new SpacedRepetitionService();

    private String flashcardId;
    private LocalDateTime lastCorrectAt;
    private int correctCount;
    private int wrongCount;
    private int level;

    public FlashcardStatistics() {
    }

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
    }

    public void applyRating(int rating, boolean wasDue) {
        this.level = SPACED_REPETITION.applyRating(this.level, rating, wasDue);
    }

    public void incrementWrong() {
        this.wrongCount++;
        this.level = SPACED_REPETITION.levelAfterWrongAnswer();
    }

    @JsonIgnore
    public boolean isDue() {
        return SPACED_REPETITION.isDue(this.level, this.lastCorrectAt);
    }
}
