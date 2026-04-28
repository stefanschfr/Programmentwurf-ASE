package de.sagaweschaefer.flashcard.menu.flashcardcreation;

import de.sagaweschaefer.flashcard.application.usecase.flashcard.AddFlashcardToSetUseCase;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.util.MenuUtils;

import java.util.ArrayList;
import java.util.List;

public class FlashcardCreationMenuHelper {
    private final FlashcardSet flashcardSet;
    private final AddFlashcardToSetUseCase addFlashcardToSetUseCase;

    public FlashcardCreationMenuHelper(FlashcardSet flashcardSet, AddFlashcardToSetUseCase addFlashcardToSetUseCase) {
        this.flashcardSet = flashcardSet;
        this.addFlashcardToSetUseCase = addFlashcardToSetUseCase;
    }

    public void addFreeTextFlashcard() {
        String question = promptQuestion();
        String answer = MenuUtils.promptForString("Antworttext eingeben: ");
        addFlashcard(new Flashcard(question, answer));
    }

    public void addMultipleChoiceFlashcard() {
        String question = promptQuestion();
        String correctAnswer = MenuUtils.promptForString("Korrekte Antwort eingeben: ");
        int wrongOptionCount = MenuUtils.promptForInt("Wie viele falsche Optionen möchten Sie hinzufügen? ");
        List<String> options = new ArrayList<>();
        options.add(correctAnswer);
        for (int i = 0; i < wrongOptionCount; i++) {
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
        double numAnswer = MenuUtils.promptForDouble("Numerische Antwort eingeben: ");
        addFlashcard(new Flashcard(question, numAnswer));
    }

    private String promptQuestion() {
        return MenuUtils.promptForString("Frage eingeben: ");
    }

    private void addFlashcard(Flashcard flashcard) {
        try {
            addFlashcardToSetUseCase.execute(flashcardSet, flashcard);
            System.out.println("Frage erfolgreich hinzugefügt!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
