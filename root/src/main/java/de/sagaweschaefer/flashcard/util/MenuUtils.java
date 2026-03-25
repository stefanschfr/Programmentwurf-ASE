package src.main.java.de.sagaweschaefer.flashcard.util;

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
}
