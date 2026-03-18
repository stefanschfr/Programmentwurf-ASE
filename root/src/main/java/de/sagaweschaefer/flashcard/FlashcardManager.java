package src.main.java.de.sagaweschaefer.flashcard;

import src.main.java.de.sagaweschaefer.flashcard.model.FlashcardSet;

import java.util.ArrayList;
import java.util.List;

public class FlashcardManager {
    private List<FlashcardSet> flashcardSets = new ArrayList<>();

    public void addFlashcardSet(String name) {
        FlashcardSet set = new FlashcardSet(name);
        flashcardSets.add(set);
        System.out.println("Lernkartenset '" + set.getName() + "' wurde erfolgreich erstellt!");
    }

    public void listFlashcardSets() {
        if (flashcardSets.isEmpty()) {
            System.out.println("Es wurden noch keine Lernkartensets erstellt.");
        } else {
            System.out.println("\n--- Alle Lernkartensets ---");
            for (int i = 0; i < flashcardSets.size(); i++) {
                System.out.println((i + 1) + ". " + flashcardSets.get(i).getName());
            }
        }
    }
}