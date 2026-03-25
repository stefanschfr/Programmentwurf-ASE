package src.main.java.de.sagaweschaefer.flashcard.menu.flashcardcreation;

import src.main.java.de.sagaweschaefer.flashcard.model.Flashcard;
import src.main.java.de.sagaweschaefer.flashcard.model.FlashcardSet;
import src.main.java.de.sagaweschaefer.flashcard.util.BinaryStorage;
import src.main.java.de.sagaweschaefer.flashcard.util.MenuUtils;

import java.util.ArrayList;
import java.util.List;

public class FlashcardCreationMenuHelper {
    private final FlashcardSet flashcardSet;
    private final List<FlashcardSet> allSets;
    private final BinaryStorage storage = new BinaryStorage();

    public FlashcardCreationMenuHelper(FlashcardSet flashcardSet, List<FlashcardSet> allSets) {
        this.flashcardSet = flashcardSet;
        this.allSets = allSets;
    }

    public void addFreeTextFlashcard() {
        String question = MenuUtils.promptForString("Frage eingeben: ");
        String answer = MenuUtils.promptForString("Antworttext eingeben: ");
        addFlashcard(new Flashcard(question, answer));
    }

    public void addMultipleChoiceFlashcard() {
        String question = MenuUtils.promptForString("Frage eingeben: ");
        String correctAnswer = MenuUtils.promptForString("Korrekte Antwort eingeben: ");
        int numOptions = MenuUtils.promptForInt("Wie viele falsche Optionen möchten Sie hinzufügen? ");
        List<String> options = new ArrayList<>();
        options.add(correctAnswer);
        for (int i = 0; i < numOptions; i++) {
            options.add(MenuUtils.promptForString("Falsche Option " + (i + 1) + ": "));
        }
        addFlashcard(new Flashcard(question, correctAnswer, options));
    }

    public void addTrueFalseFlashcard() {
        String question = MenuUtils.promptForString("Frage eingeben: ");
        boolean isTrue = MenuUtils.promptForString("Ist die Aussage wahr? (j/n): ").equalsIgnoreCase("j");
        addFlashcard(new Flashcard(question, isTrue));
    }

    public void addNumericFlashcard() {
        String question = MenuUtils.promptForString("Frage eingeben: ");
        try {
            double numAnswer = Double.parseDouble(MenuUtils.promptForString("Numerische Antwort eingeben: "));
            addFlashcard(new Flashcard(question, numAnswer));
        } catch (NumberFormatException e) {
            System.out.println("Ungültige Zahl! Frage wurde nicht erstellt.");
        }
    }

    private void addFlashcard(Flashcard flashcard) {
        flashcardSet.getFlashcardSet().add(flashcard);
        save();
        System.out.println("Frage erfolgreich hinzugefügt!");
    }

    private void save() {
        storage.saveFlashcardSets(allSets);
    }
}
