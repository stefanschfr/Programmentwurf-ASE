package src.main.java.de.sagaweschaefer.flashcard.menu.flashcardmanager;

import src.main.java.de.sagaweschaefer.flashcard.menu.flashcardcreation.FlashcardCreationMenu;
import src.main.java.de.sagaweschaefer.flashcard.menu.flashcardcreation.FlashcardCreationMenuHelper;
import src.main.java.de.sagaweschaefer.flashcard.model.Flashcard;
import src.main.java.de.sagaweschaefer.flashcard.model.FlashcardSet;
import src.main.java.de.sagaweschaefer.flashcard.util.BinaryStorage;
import src.main.java.de.sagaweschaefer.flashcard.util.MenuUtils;

import java.util.List;

public class FlashcardManagerMenuHelper {
    private final FlashcardSet flashcardSet;
    private final List<FlashcardSet> allSets;
    private final BinaryStorage storage = new BinaryStorage();

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
        List<Flashcard> flashcards = flashcardSet.getFlashcardSet();
        if (flashcards.isEmpty()) {
            System.out.println("Dieses Set enthält noch keine Fragen.");
        } else {
            System.out.println("\n--- Fragen in '" + flashcardSet.getName() + "' ---");
            for (int i = 0; i < flashcards.size(); i++) {
                Flashcard f = flashcards.get(i);
                System.out.println((i + 1) + ". [" + f.getQuestionType() + "] " + f.getQuestion());
            }
        }
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
