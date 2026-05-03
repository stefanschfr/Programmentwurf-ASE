package de.sagaweschaefer.flashcard.domain.valueobject;

import java.util.Objects;

@SuppressWarnings("unused")
public final class DailyGoal {

    public static final int MIN_CARDS = 1;

    public static final int MAX_CARDS = 1000;

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


