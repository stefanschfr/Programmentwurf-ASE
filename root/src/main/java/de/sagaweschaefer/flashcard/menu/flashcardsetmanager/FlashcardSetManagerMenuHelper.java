package src.main.java.de.sagaweschaefer.flashcard.menu.flashcardsetmanager;

import src.main.java.de.sagaweschaefer.flashcard.menu.flashcardmanager.FlashcardManagerMenu;
import src.main.java.de.sagaweschaefer.flashcard.menu.flashcardmanager.FlashcardManagerMenuHelper;
import src.main.java.de.sagaweschaefer.flashcard.model.FlashcardSet;
import src.main.java.de.sagaweschaefer.flashcard.util.BinaryStorage;
import src.main.java.de.sagaweschaefer.flashcard.util.MenuUtils;

import java.util.ArrayList;
import java.util.List;

public class FlashcardSetManagerMenuHelper {
    private List<FlashcardSet> flashcardSets;
    private final BinaryStorage storage = new BinaryStorage();

    public FlashcardSetManagerMenuHelper() {
        this.flashcardSets = storage.loadFlashcardSets();
    }

    public void addFlashcardSet() {
        String name = MenuUtils.promptForString("Name des Lernkartensets: ");
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

    public void deleteFlashcardSet() {
        listFlashcardSets();
        if (flashcardSets.isEmpty()) return;

        int index = MenuUtils.promptForInt("Geben Sie die Nummer des Sets ein, das gelöscht werden soll: ") - 1;
        if (index < 0 || index >= flashcardSets.size()) {
            System.out.println("Ungültige Auswahl! Kein Set gelöscht.");
            return;
        }

        FlashcardSet removed = flashcardSets.remove(index);
        storage.saveFlashcardSets(flashcardSets);
        System.out.println("Lernkartenset '" + removed.getName() + "' wurde gelöscht.");
    }

    public void editFlashcardSet() {
        listFlashcardSets();
        if (flashcardSets.isEmpty()) return;

        int choice = MenuUtils.promptForInt("Geben Sie die Nummer des Sets ein, das bearbeitet werden soll: ") - 1;
        if (choice >= 0 && choice < flashcardSets.size()) {
            var set = flashcardSets.get(choice);
            var flashcardManagerHelper = new FlashcardManagerMenuHelper(set, flashcardSets);
            new FlashcardManagerMenu(flashcardManagerHelper).start();
        } else {
            System.out.println("Ungültige Auswahl!");
        }
    }
}