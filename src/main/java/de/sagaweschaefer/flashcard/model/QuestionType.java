package de.sagaweschaefer.flashcard.model;

public enum QuestionType {
    FREE_TEXT("Freitext"),
    MULTIPLE_CHOICE("Multiple Choice"),
    TRUE_FALSE("Wahr/Falsch"),
    NUMERIC("Numerisch");

    private final String displayName;

    QuestionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
