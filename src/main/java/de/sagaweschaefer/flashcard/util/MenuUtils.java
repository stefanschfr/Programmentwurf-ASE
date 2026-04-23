package de.sagaweschaefer.flashcard.util;

import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;

import java.util.List;
import java.util.Map;

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

    public static double promptForDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = AppScanner.SCANNER.nextLine();

            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Ungültige Zahl! Bitte erneut eingeben.");
            }
        }
    }

    public static <T> T selectFromList(List<T> list, String prompt) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        int index = promptForInt(prompt) - 1;
        if (index >= 0 && index < list.size()) {
            return list.get(index);
        }
        System.out.println("Ungültige Auswahl.");
        return null;
    }

    public static <T> int selectIndexFromList(List<T> list, String prompt) {
        if (list == null || list.isEmpty()) {
            return -1;
        }
        int index = promptForInt(prompt) - 1;
        if (index >= 0 && index < list.size()) {
            return index;
        }
        System.out.println("Ungültige Auswahl.");
        return -1;
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

    public static void displayFlashcards(List<Flashcard> cards, Map<String, FlashcardStatistics> statistics, String title) {
        if (cards.isEmpty()) {
            System.out.println("Dieses Set enthält noch keine Fragen.");
        } else {
            System.out.println("\n--- " + title + " ---");
            System.out.printf("%-3s | %-15s | %-30s | %-20s | %-10s%n", "Nr.", "Typ", "Frage", "Antwort", "Lernstufe");
            System.out.println("----------------------------------------------------------------------------------------------------");
            for (int i = 0; i < cards.size(); i++) {
                Flashcard f = cards.get(i);
                FlashcardStatistics stats = (statistics != null) ? statistics.get(f.getId()) : null;
                int level = (stats != null) ? stats.getLevel() : 0;

                String answer = f.getCorrectAnswerDisplay();

                System.out.printf("%-3d | %-15s | %-30s | %-20s | %-10d%n",
                        (i + 1),
                        f.getQuestionType().getDisplayName(),
                        truncate(f.getQuestion(), 30),
                        truncate(answer, 20),
                        level);
            }
        }
    }

    private static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }
}
