package src.main.java.de.sagaweschaefer.flashcard.menu;

import src.main.java.de.sagaweschaefer.flashcard.FlashcardManager;
import src.main.java.de.sagaweschaefer.flashcard.util.AppScanner;
import src.main.java.de.sagaweschaefer.flashcard.util.MenuUtils;

public class FlashcardManagerMenu extends Menu {

    private FlashcardManager flashcardManager = new FlashcardManager();

    @Override
    protected void showMenu() {
        System.out.println("\n=== Flashcard Manager ===");
        System.out.println("1. Neues Lernkarten-Set erstellen");
        System.out.println("2. Alle Lernkarten-Sets anzeigen");
        System.out.println("3. Lernkartenset löschen");
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
            case 3 -> {
                flashcardManager.listFlashcardSets();
                if (flashcardManager.getFlashcardSets().isEmpty()) yield true;

                System.out.print("Geben Sie die Nummer des Sets ein, das gelöscht werden soll: ");
                int choice = MenuUtils.readMenuSelection() - 1; // Liste beginnt bei 1
                if (!flashcardManager.deleteFlashcardSet(choice)) {
                    System.out.println("Ungültige Auswahl! Kein Set gelöscht.");
                }
                yield true;
            }
            default -> {
                System.out.println("Ungültige Eingabe! Bitte wählen Sie eine gültige Option.");
                yield true;
            }
        };
    }
}