package src.main.java.de.sagaweschaefer.flashcard.util;

public class MenuUtils {
    public static int readMenuSelection() {
        try {
            return Integer.parseInt(AppScanner.SCANNER.nextLine());
        } catch (NumberFormatException e) {
            return -1; // Ungültige Eingabe
        }
    }
}
