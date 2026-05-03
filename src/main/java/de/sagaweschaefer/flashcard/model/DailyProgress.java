package de.sagaweschaefer.flashcard.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused")
public class DailyProgress implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private LocalDate date;
    private int goalCards;
    private int learnedCards;
    private int correctCards;

    public DailyProgress() {
    }

    public DailyProgress(LocalDate date, int goalCards) {
        this.date = date;
        this.goalCards = goalCards;
        this.learnedCards = 0;
        this.correctCards = 0;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getGoalCards() {
        return goalCards;
    }

    public void setGoalCards(int goalCards) {
        this.goalCards = goalCards;
    }

    public int getLearnedCards() {
        return learnedCards;
    }

    public void setLearnedCards(int learnedCards) {
        this.learnedCards = learnedCards;
    }

    public int getCorrectCards() {
        return correctCards;
    }

    public void setCorrectCards(int correctCards) {
        this.correctCards = correctCards;
    }

    public void recordCard(boolean correct) {
        this.learnedCards++;
        if (correct) {
            this.correctCards++;
        }
    }

    public boolean isGoalReached() {
        return goalCards > 0 && learnedCards >= goalCards;
    }

    public double getProgressRatio() {
        if (goalCards <= 0) {
            return 0.0;
        }
        double ratio = (double) learnedCards / goalCards;
        return Math.min(1.0, ratio);
    }
}

