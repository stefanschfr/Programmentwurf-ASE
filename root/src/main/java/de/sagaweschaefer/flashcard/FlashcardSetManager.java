package src.main.java.de.sagaweschaefer.flashcard;

import src.main.java.de.sagaweschaefer.flashcard.model.FlashcardSet;
import src.main.java.de.sagaweschaefer.flashcard.util.BinaryStorage;

import java.util.ArrayList;
import java.util.List;

public class FlashcardSetManager {
    private List<FlashcardSet> flashcardSets = new ArrayList<>();
    private final BinaryStorage storage = new BinaryStorage();

    public FlashcardSetManager() {
        this.flashcardSets = storage.loadFlashcardSets();
    }

    public void addFlashcardSet(String name) {
        FlashcardSet set = new FlashcardSet(name);
        flashcardSets.add(set);
        storage.saveFlashcardSets(flashcardSets);
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

    public boolean deleteFlashcardSet(int index) {
        if (index < 0 || index >= flashcardSets.size()) {
            System.out.println("Ungültige Auswahl! Kein Set gelöscht.");
            return false;
        }

        FlashcardSet removed = flashcardSets.remove(index);
        storage.saveFlashcardSets(flashcardSets);
        System.out.println("Lernkartenset '" + removed.getName() + "' wurde gelöscht.");
        return true;
    }

    public List<FlashcardSet> getFlashcardSets() {
        return flashcardSets;
    }
}