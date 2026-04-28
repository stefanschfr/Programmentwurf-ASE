package de.sagaweschaefer.flashcard.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FlashcardSet implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private FlashcardSetName name;
    private List<Flashcard> flashcards;

    public FlashcardSet() {
        this.flashcards = new ArrayList<>();
    }

    @JsonCreator
    public FlashcardSet(@JsonProperty("name") String name,
                        @JsonProperty("flashcards") List<Flashcard> flashcards) {
        this.flashcards = flashcards == null ? new ArrayList<>() : flashcards;
        this.name = name == null ? null : FlashcardSetName.of(name);
    }

    // Konstruktor mit Namen
    public FlashcardSet(String name) {
        this(name, new ArrayList<>());
    }

    public FlashcardSet(FlashcardSetName name) {
        this(name.value(), new ArrayList<>());
    }

    @JsonProperty("name")
    public String getName() {
        return name == null ? null : name.value();
    }

    @JsonIgnore
    public FlashcardSetName getSetName() {
        return name;
    }

    public void renameTo(FlashcardSetName name) {
        this.name = name;
    }

    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    public void setFlashcards(List<Flashcard> flashcards) {
        this.flashcards = flashcards;
    }
}