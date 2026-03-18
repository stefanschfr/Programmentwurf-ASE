package src.main.java.de.sagaweschaefer.flashcard.model;

import java.util.ArrayList;
import java.util.List;

public class FlashcardSet {

    private String name;
    private List<Flashcard> flashcardSet;

    // Standard-Konstruktor (z.B. für JSON)
    public FlashcardSet() {
        this.flashcardSet = new ArrayList<>();
    }

    // Konstruktor mit Namen
    public FlashcardSet(String name) {
        this.name = name;
        this.flashcardSet = new ArrayList<>();
    }
}
