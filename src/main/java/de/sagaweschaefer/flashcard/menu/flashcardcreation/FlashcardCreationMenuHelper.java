package de.sagaweschaefer.flashcard.menu.flashcardcreation;

import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.util.FlashcardStorage;
import de.sagaweschaefer.flashcard.util.MenuUtils;

import java.util.ArrayList;
import java.util.List;

public class FlashcardCreationMenuHelper {
    private final FlashcardSet flashcardSet;
    private final List<FlashcardSet> allSets;
    private final FlashcardStorage storage;

    public FlashcardCreationMenuHelper(FlashcardSet flashcardSet, List<FlashcardSet> allSets, FlashcardStorage storage) {
        this.flashcardSet = flashcardSet;
        this.allSets = allSets;
        this.storage = storage;
    }

    public void addFreeTextFlashcard() {
        String question = promptQuestion();
        String answer = MenuUtils.promptForString("Antworttext eingeben: ");
        addFlashcard(new Flashcard(question, answer));
    }

    public void addMultipleChoiceFlashcard() {
        String question = promptQuestion();
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
        String question = promptQuestion();
        boolean isTrue = MenuUtils.promptForString("Ist die Aussage wahr? (j/n): ").equalsIgnoreCase("j");
        addFlashcard(new Flashcard(question, isTrue));
    }

    public void addNumericFlashcard() {
        String question = promptQuestion();
        try {
            double numAnswer = Double.parseDouble(MenuUtils.promptForString("Numerische Antwort eingeben: "));
            addFlashcard(new Flashcard(question, numAnswer));
        } catch (NumberFormatException e) {
            System.out.println("Ungültige Zahl! Frage wurde nicht erstellt.");
        }
    }

    private String promptQuestion() {
        return MenuUtils.promptForString("Frage eingeben: ");
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
