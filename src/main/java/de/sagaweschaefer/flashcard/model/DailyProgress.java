package de.sagaweschaefer.flashcard.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Entity: Lernfortschritt für einen einzelnen Tag.
 *
 * <p>Trackt, wie viele Karten an einem konkreten Datum bereits gelernt
 * wurden, sowie wie viele davon korrekt beantwortet waren. Wird vom
 * Daily-Goal-Repository pro Tag persistiert.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused") // Setter werden via Reflection durch Jackson aufgerufen
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

    /** Erhöht den Zähler der gelernten Karten und ggf. der korrekten. */
    public void recordCard(boolean correct) {
        this.learnedCards++;
        if (correct) {
            this.correctCards++;
        }
    }

    /** Liefert {@code true} wenn das Ziel für den Tag erreicht oder übertroffen ist. */
    public boolean isGoalReached() {
        return goalCards > 0 && learnedCards >= goalCards;
    }

    /**
     * Liefert den Fortschritt als Wert zwischen 0.0 und 1.0 (1.0 = Ziel erreicht).
     * Werte über 100 % werden auf 1.0 begrenzt.
     */
    public double getProgressRatio() {
        if (goalCards <= 0) {
            return 0.0;
        }
        double ratio = (double) learnedCards / goalCards;
        return Math.min(1.0, ratio);
    }
}

