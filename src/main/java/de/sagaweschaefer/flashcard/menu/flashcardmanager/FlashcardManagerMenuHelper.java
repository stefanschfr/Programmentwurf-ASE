package de.sagaweschaefer.flashcard.menu.flashcardmanager;

import de.sagaweschaefer.flashcard.menu.flashcardcreation.FlashcardCreationMenu;
import de.sagaweschaefer.flashcard.menu.flashcardcreation.FlashcardCreationMenuHelper;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.util.JsonStorage;
import de.sagaweschaefer.flashcard.util.MenuUtils;

import java.util.List;

public class FlashcardManagerMenuHelper {
    private final FlashcardSet flashcardSet;
    private final List<FlashcardSet> allSets;
    private final JsonStorage storage = new JsonStorage();

    public FlashcardManagerMenuHelper(FlashcardSet flashcardSet, List<FlashcardSet> allSets) {
        this.flashcardSet = flashcardSet;
        this.allSets = allSets;
    }

    public void addFlashcard() {
        FlashcardCreationMenuHelper creationHelper = new FlashcardCreationMenuHelper(flashcardSet, allSets);
        FlashcardCreationMenu creationMenu = new FlashcardCreationMenu(creationHelper);
        creationMenu.start();
    }

    public void listFlashcards() {
        MenuUtils.displayFlashcards(flashcardSet.getFlashcardSet(), "Fragen in '" + flashcardSet.getName() + "'");
    }

    public void deleteFlashcard() {
        listFlashcards();
        if (flashcardSet.getFlashcardSet().isEmpty()) return;

        int index = MenuUtils.promptForInt("Geben Sie die Nummer der Frage ein, die gelöscht werden soll: ") - 1;
        List<Flashcard> flashcards = flashcardSet.getFlashcardSet();
        if (index < 0 || index >= flashcards.size()) {
            System.out.println("Ungültige Auswahl! Keine Frage gelöscht.");
            return;
        }

        Flashcard removed = flashcards.remove(index);
        save();
        System.out.println("Frage '" + removed.getQuestion() + "' wurde gelöscht.");
    }

    private void save() {
        storage.saveFlashcardSets(allSets);
    }

    public FlashcardSet getFlashcardSet() {
        return flashcardSet;
    }
}
