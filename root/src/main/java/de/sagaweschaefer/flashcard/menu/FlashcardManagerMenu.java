package src.main.java.de.sagaweschaefer.flashcard.menu;

import src.main.java.de.sagaweschaefer.flashcard.FlashcardManager;
import src.main.java.de.sagaweschaefer.flashcard.model.Flashcard;
import src.main.java.de.sagaweschaefer.flashcard.util.AppScanner;
import src.main.java.de.sagaweschaefer.flashcard.util.MenuUtils;

import java.util.ArrayList;
import java.util.List;

public class FlashcardManagerMenu extends Menu {
    private final FlashcardManager flashcardManager;

    public FlashcardManagerMenu(FlashcardManager flashcardManager) {
        this.flashcardManager = flashcardManager;
    }

    @Override
    protected void showMenu() {
        System.out.println("\n=== Flashcard Manager für Set: " + flashcardManager.getFlashcardSet().getName() + " ===");
        System.out.println("1. Neue Frage hinzufügen");
        System.out.println("2. Alle Fragen anzeigen");
        System.out.println("3. Frage löschen");
        System.out.println("0. Zurück");
        System.out.print("Bitte wählen Sie eine Option: ");
    }

    @Override
    protected boolean handleSelection(int selection) {
        return switch (selection) {
            case 0 -> false;
            case 1 -> {
                addFlashcardFlow();
                yield true;
            }
            case 2 -> {
                flashcardManager.listFlashcards();
                yield true;
            }
            case 3 -> {
                flashcardManager.listFlashcards();
                if (flashcardManager.getFlashcardSet().getFlashcardSet().isEmpty()) yield true;
                System.out.print("Geben Sie die Nummer der Frage ein, die gelöscht werden soll: ");
                int choice = MenuUtils.readMenuSelection() - 1;
                flashcardManager.deleteFlashcard(choice);
                yield true;
            }
            default -> {
                System.out.println("Ungültige Eingabe!");
                yield true;
            }
        };
    }

    private void addFlashcardFlow() {
        System.out.println("\n--- Welchen Fragentyp möchten Sie hinzufügen? ---");
        System.out.println("1. Freitext");
        System.out.println("2. Multiple Choice");
        System.out.println("3. Wahr/Falsch");
        System.out.println("4. Numerisch");
        System.out.print("Wahl: ");
        int typeChoice = MenuUtils.readMenuSelection();

        System.out.print("Frage eingeben: ");
        String question = AppScanner.SCANNER.nextLine();

        switch (typeChoice) {
            case 1 -> {
                System.out.print("Antworttext eingeben: ");
                String answer = AppScanner.SCANNER.nextLine();
                flashcardManager.addFlashcard(new Flashcard(question, answer));
            }
            case 2 -> {
                System.out.print("Korrekte Antwort eingeben: ");
                String correctAnswer = AppScanner.SCANNER.nextLine();
                System.out.print("Wie viele falsche Optionen möchten Sie hinzufügen? ");
                int numOptions = MenuUtils.readMenuSelection();
                List<String> options = new ArrayList<>();
                options.add(correctAnswer);
                for (int i = 0; i < numOptions; i++) {
                    System.out.print("Falsche Option " + (i + 1) + ": ");
                    options.add(AppScanner.SCANNER.nextLine());
                }
                flashcardManager.addFlashcard(new Flashcard(question, correctAnswer, options));
            }
            case 3 -> {
                System.out.print("Ist die Aussage wahr? (j/n): ");
                boolean isTrue = AppScanner.SCANNER.nextLine().equalsIgnoreCase("j");
                flashcardManager.addFlashcard(new Flashcard(question, isTrue));
            }
            case 4 -> {
                System.out.print("Numerische Antwort eingeben: ");
                try {
                    double numAnswer = Double.parseDouble(AppScanner.SCANNER.nextLine());
                    flashcardManager.addFlashcard(new Flashcard(question, numAnswer));
                } catch (NumberFormatException e) {
                    System.out.println("Ungültige Zahl! Frage wurde nicht erstellt.");
                }
            }
            default -> System.out.println("Ungültiger Typ!");
        }
    }
}
