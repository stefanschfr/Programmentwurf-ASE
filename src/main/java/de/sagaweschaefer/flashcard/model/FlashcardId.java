package de.sagaweschaefer.flashcard.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public final class FlashcardId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String value;

    private FlashcardId(String value) {
        this.value = value;
    }

    public static FlashcardId random() {
        return new FlashcardId(UUID.randomUUID().toString());
    }

    @JsonCreator
    public static FlashcardId of(String rawValue) {
        if (rawValue == null || rawValue.trim().isEmpty()) {
            throw new IllegalArgumentException("Die Flashcard-ID darf nicht leer sein.");
        }
        return new FlashcardId(rawValue.trim());
    }

    @JsonValue
    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof FlashcardId that)) {
            return false;
        }
        return value.equals(that.value);
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

