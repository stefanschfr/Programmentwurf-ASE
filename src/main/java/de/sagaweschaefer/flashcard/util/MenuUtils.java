package de.sagaweschaefer.flashcard.util;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import java.util.List;

public class MenuUtils {
    public static int readMenuSelection() {
        try {
            return Integer.parseInt(AppScanner.SCANNER.nextLine());
        } catch (NumberFormatException e) {
            return -1; // Ungültige Eingabe
        }
    }

    public static String promptForString(String prompt) {
        System.out.print(prompt);
        return AppScanner.SCANNER.nextLine();
    }

    public static int promptForInt(String prompt) {
        System.out.print(prompt);
        return readMenuSelection();
    }

    public static void displayFlashcardSets(List<FlashcardSet> sets, String title) {
        if (sets.isEmpty()) {
            System.out.println("Es wurden noch keine Lernkartensets erstellt.");
        } else {
            System.out.println("\n--- " + title + " ---");
            for (int i = 0; i < sets.size(); i++) {
                System.out.println((i + 1) + ". " + sets.get(i).getName());
            }
        }
    }

    public static void displayFlashcards(List<de.sagaweschaefer.flashcard.model.Flashcard> cards, String title) {
        if (cards.isEmpty()) {
            System.out.println("Dieses Set enthält noch keine Fragen.");
        } else {
            System.out.println("\n--- " + title + " ---");
            for (int i = 0; i < cards.size(); i++) {
                var f = cards.get(i);
                System.out.println((i + 1) + ". [" + f.getQuestionType() + "] " + f.getQuestion());
            }
        }
    }
}
