package de.sagaweschaefer.flashcard.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public final class FlashcardId {
    private final String value;

    public FlashcardId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("FlashcardId darf nicht leer sein.");
        }
        this.value = value;
    }

    public static FlashcardId generate() {
        return new FlashcardId(UUID.randomUUID().toString());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlashcardId that = (FlashcardId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}

