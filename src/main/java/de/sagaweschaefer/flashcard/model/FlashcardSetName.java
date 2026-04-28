package de.sagaweschaefer.flashcard.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;

public final class FlashcardSetName implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String value;
    private final String comparisonValue;

    private FlashcardSetName(String value) {
        this.value = value;
        this.comparisonValue = value.toLowerCase(Locale.ROOT);
    }

    public static FlashcardSetName of(String rawValue) {
        if (rawValue == null || rawValue.trim().isEmpty()) {
            throw new IllegalArgumentException("Der Name des Lernkartensets darf nicht leer sein.");
        }
        return new FlashcardSetName(rawValue.trim());
    }

    public String value() {
        return value;
    }

    public boolean matches(String rawValue) {
        if (rawValue == null || rawValue.trim().isEmpty()) {
            return false;
        }
        return comparisonValue.equals(rawValue.trim().toLowerCase(Locale.ROOT));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof FlashcardSetName that)) {
            return false;
        }
        return comparisonValue.equals(that.comparisonValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(comparisonValue);
    }

    @Override
    public String toString() {
        return value;
    }
}

