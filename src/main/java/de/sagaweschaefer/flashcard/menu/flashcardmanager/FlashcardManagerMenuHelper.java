package de.sagaweschaefer.flashcard.menu.flashcardmanager;

import de.sagaweschaefer.flashcard.menu.flashcardcreation.FlashcardCreationMenu;
import de.sagaweschaefer.flashcard.menu.flashcardcreation.FlashcardCreationMenuHelper;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;
import de.sagaweschaefer.flashcard.util.FlashcardStorage;
import de.sagaweschaefer.flashcard.util.MenuUtils;

import java.util.List;
import java.util.Map;

public class FlashcardManagerMenuHelper {
    private final FlashcardSet flashcardSet;
    private final List<FlashcardSet> allSets;
    private final FlashcardStorage storage;

    public FlashcardManagerMenuHelper(FlashcardSet flashcardSet, List<FlashcardSet> allSets, FlashcardStorage storage) {
        this.flashcardSet = flashcardSet;
        this.allSets = allSets;
        this.storage = storage;
    }

    public void addFlashcard() {
        FlashcardCreationMenuHelper creationHelper = new FlashcardCreationMenuHelper(flashcardSet, allSets, storage);
        FlashcardCreationMenu creationMenu = new FlashcardCreationMenu(creationHelper);
        creationMenu.start();
    }

    public void listFlashcards() {
        Map<String, FlashcardStatistics> statistics = storage.loadStatistics();
        MenuUtils.displayFlashcards(flashcardSet.getFlashcardSet(), statistics, "Fragen in '" + flashcardSet.getName() + "'");
    }

    public void deleteFlashcard() {
        listFlashcards();
        List<Flashcard> flashcards = flashcardSet.getFlashcardSet();
        if (flashcards.isEmpty()) return;

        int index = MenuUtils.selectIndexFromList(flashcards, "Geben Sie die Nummer der Frage ein, die gelöscht werden soll: ");
        if (index == -1) {
            System.out.println("Ungültige Auswahl! Keine Frage gelöscht.");
            return;
        }

        Flashcard removed = flashcards.remove(index);

        // Verwaiste Statistiken entfernen
        Map<String, FlashcardStatistics> statisticsMap = storage.loadStatistics();
        if (statisticsMap.remove(removed.getId()) != null) {
            storage.saveStatistics(statisticsMap);
        }

        storage.saveFlashcardSets(allSets);
        System.out.println("Frage '" + removed.getQuestion() + "' wurde gelöscht.");
    }
}
