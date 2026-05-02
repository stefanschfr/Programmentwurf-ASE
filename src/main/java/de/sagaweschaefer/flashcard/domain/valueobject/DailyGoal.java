package de.sagaweschaefer.flashcard.domain.valueobject;

import java.util.Objects;

/**
 * Value Object: Tägliches Lernziel (Anzahl Karten pro Tag).
 *
 * <p>Immutabel und durch Wert-Gleichheit definiert. Validiert beim Anlegen,
 * dass der Wert positiv und nicht unsinnig groß ist.</p>
 */
@SuppressWarnings("unused") // Public Domain-API: wird via UseCases/Menu verwendet
public final class DailyGoal {

    /** Minimal erlaubtes Tagesziel. */
    public static final int MIN_CARDS = 1;

    /** Maximal sinnvolles Tagesziel (Hartgrenze gegen Tippfehler). */
    public static final int MAX_CARDS = 1000;

    /** Standard-Tagesziel, falls kein Ziel gesetzt wurde. */
    public static final int DEFAULT_CARDS = 20;

    private final int cardsPerDay;

    public DailyGoal(int cardsPerDay) {
        if (cardsPerDay < MIN_CARDS || cardsPerDay > MAX_CARDS) {
            throw new IllegalArgumentException(
                    "DailyGoal muss zwischen " + MIN_CARDS + " und " + MAX_CARDS + " liegen, war: " + cardsPerDay);
        }
        this.cardsPerDay = cardsPerDay;
    }

    public static DailyGoal defaultGoal() {
        return new DailyGoal(DEFAULT_CARDS);
    }

    public int getCardsPerDay() {
        return cardsPerDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyGoal dailyGoal = (DailyGoal) o;
        return cardsPerDay == dailyGoal.cardsPerDay;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardsPerDay);
    }

    @Override
    public String toString() {
        return cardsPerDay + " Karten/Tag";
    }
}


