package src.main.java.de.sagaweschaefer.flashcard.menu;

import src.main.java.de.sagaweschaefer.flashcard.model.Flashcard;
import src.main.java.de.sagaweschaefer.flashcard.model.FlashcardSet;
import src.main.java.de.sagaweschaefer.flashcard.util.BinaryStorage;
import src.main.java.de.sagaweschaefer.flashcard.util.MenuUtils;

import java.util.ArrayList;
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
        System.out.println("\n--- Welchen Fragentyp möchten Sie hinzufügen? ---");
        System.out.println("1. Freitext");
        System.out.println("2. Multiple Choice");
        System.out.println("3. Wahr/Falsch");
        System.out.println("4. Numerisch");
        int typeChoice = MenuUtils.promptForInt("Wahl: ");

        String question = MenuUtils.promptForString("Frage eingeben: ");

        switch (typeChoice) {
            case 1 -> {
                String answer = MenuUtils.promptForString("Antworttext eingeben: ");
                addFlashcard(new Flashcard(question, answer));
            }
            case 2 -> {
                String correctAnswer = MenuUtils.promptForString("Korrekte Antwort eingeben: ");
                int numOptions = MenuUtils.promptForInt("Wie viele falsche Optionen möchten Sie hinzufügen? ");
                List<String> options = new ArrayList<>();
                options.add(correctAnswer);
                for (int i = 0; i < numOptions; i++) {
                    options.add(MenuUtils.promptForString("Falsche Option " + (i + 1) + ": "));
                }
                addFlashcard(new Flashcard(question, correctAnswer, options));
            }
            case 3 -> {
                boolean isTrue = MenuUtils.promptForString("Ist die Aussage wahr? (j/n): ").equalsIgnoreCase("j");
                addFlashcard(new Flashcard(question, isTrue));
            }
            case 4 -> {
                try {
                    double numAnswer = Double.parseDouble(MenuUtils.promptForString("Numerische Antwort eingeben: "));
                    addFlashcard(new Flashcard(question, numAnswer));
                } catch (NumberFormatException e) {
                    System.out.println("Ungültige Zahl! Frage wurde nicht erstellt.");
                }
            }
            default -> System.out.println("Ungültiger Typ!");
        }
    }

    private void addFlashcard(Flashcard flashcard) {
        flashcardSet.getFlashcardSet().add(flashcard);
        save();
        System.out.println("Frage erfolgreich hinzugefügt!");
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
