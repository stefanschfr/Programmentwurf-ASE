package de.sagaweschaefer.flashcard.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FlashcardSet implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private List<Flashcard> flashcards;

    public FlashcardSet() {
        this.flashcards = new ArrayList<>();
    }

    // Konstruktor mit Namen
    public FlashcardSet(String name) {
        this.name = name;
        this.flashcards = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    public void setFlashcards(List<Flashcard> flashcards) {
        this.flashcards = flashcards;
    }
}