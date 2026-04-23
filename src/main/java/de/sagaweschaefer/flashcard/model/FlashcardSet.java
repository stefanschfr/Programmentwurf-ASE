package de.sagaweschaefer.flashcard.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FlashcardSet implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private List<Flashcard> flashcardSet;

    public FlashcardSet() {
        this.flashcardSet = new ArrayList<>();
    }

    // Konstruktor mit Namen
    public FlashcardSet(String name) {
        this.name = name;
        this.flashcardSet = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Flashcard> getFlashcardSet() {
        return flashcardSet;
    }

    public void setFlashcardSet(List<Flashcard> flashcardSet) {
        this.flashcardSet = flashcardSet;
    }
}