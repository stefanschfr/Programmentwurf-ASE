package de.sagaweschaefer.flashcard.menu.flashcardsetmanager;

import de.sagaweschaefer.flashcard.menu.flashcardmanager.FlashcardManagerMenu;
import de.sagaweschaefer.flashcard.menu.flashcardmanager.FlashcardManagerMenuHelper;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;
import de.sagaweschaefer.flashcard.util.FlashcardStorage;
import de.sagaweschaefer.flashcard.util.MenuUtils;

import java.util.List;
import java.util.Map;

public class FlashcardSetManagerMenuHelper {
    private final FlashcardStorage storage;
    private final List<FlashcardSet> flashcardSets;

    public FlashcardSetManagerMenuHelper(FlashcardStorage storage) {
        this.storage = storage;
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
        MenuUtils.displayFlashcardSets(flashcardSets, "Alle Lernkartensets");
    }

    public void deleteFlashcardSet() {
        listFlashcardSets();
        if (flashcardSets.isEmpty()) return;

        int index = MenuUtils.selectIndexFromList(flashcardSets, "Geben Sie die Nummer des Sets ein, das gelöscht werden soll: ");
        if (index != -1) {
            performDelete(index);
            storage.saveFlashcardSets(flashcardSets);
        } else {
            System.out.println("Ungültige Auswahl! Kein Set gelöscht.");
        }
    }

    private void performDelete(int index) {
        FlashcardSet removed = flashcardSets.remove(index);

        // Verwaiste Statistiken für alle Karten im Set entfernen
        Map<String, FlashcardStatistics> statisticsMap = storage.loadStatistics();
        boolean statsChanged = false;
        for (Flashcard card : removed.getFlashcardSet()) {
            if (statisticsMap.remove(card.getId()) != null) {
                statsChanged = true;
            }
        }
        if (statsChanged) {
            storage.saveStatistics(statisticsMap);
        }

        System.out.println("Lernkartenset '" + removed.getName() + "' wurde gelöscht.");
    }

    public void editFlashcardSet() {
        listFlashcardSets();
        if (flashcardSets.isEmpty()) return;

        FlashcardSet set = MenuUtils.selectFromList(flashcardSets, "Geben Sie die Nummer des Sets ein, das bearbeitet werden soll: ");
        if (set != null) {
            var flashcardManagerHelper = new FlashcardManagerMenuHelper(set, flashcardSets, storage);
            new FlashcardManagerMenu(flashcardManagerHelper, set.getName()).start();
        } else {
            System.out.println("Ungültige Auswahl!");
        }
    }
}