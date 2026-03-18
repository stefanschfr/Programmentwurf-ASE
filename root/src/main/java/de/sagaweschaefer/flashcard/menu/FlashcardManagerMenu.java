package src.main.java.de.sagaweschaefer.flashcard.menu;

import src.main.java.de.sagaweschaefer.flashcard.FlashcardManager;
import src.main.java.de.sagaweschaefer.flashcard.util.AppScanner;

public class FlashcardManagerMenu extends Menu {

    private FlashcardManager flashcardManager = new FlashcardManager();

    @Override
    protected void showMenu() {
        System.out.println("\n=== Flashcard Manager ===");
        System.out.println("1. Neues Lernkarten-Set erstellen");
        System.out.println("2. Alle Lernkarten-Sets anzeigen");
        System.out.println("0. Zurück zum Hauptmenü");
        System.out.print("Bitte wählen Sie eine Option: ");
    }

    @Override
    protected boolean handleSelection(int selection) {
        return switch (selection) {
            case 0 -> false;
            case 1 -> {
                System.out.print("Name des Lernkartensets: ");
                String name = AppScanner.SCANNER.nextLine();
                flashcardManager.addFlashcardSet(name);
                yield true;
            }
            case 2 -> {
                flashcardManager.listFlashcardSets();
                yield true;
            }
            default -> {
                System.out.println("Ungültige Eingabe! Bitte wählen Sie eine gültige Option.");
                yield true;
            }
        };
    }
}