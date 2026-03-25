package src.main.java.de.sagaweschaefer.flashcard.menu;

import src.main.java.de.sagaweschaefer.flashcard.util.AppScanner;
import src.main.java.de.sagaweschaefer.flashcard.util.MenuUtils;

public class FlashcardSetManagerMenu extends Menu {

    private FlashcardSetManagerMenuHelper flashcardSetManagerMenuHelper = new FlashcardSetManagerMenuHelper();

    @Override
    protected void showMenu() {
        System.out.println("\n=== Flashcard Set Manager ===");
        System.out.println("1. Neues Lernkarten-Set erstellen");
        System.out.println("2. Alle Lernkarten-Sets anzeigen");
        System.out.println("3. Lernkartenset bearbeiten");
        System.out.println("4. Lernkartenset löschen");
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
                flashcardSetManagerMenuHelper.addFlashcardSet(name);
                yield true;
            }
            case 2 -> {
                flashcardSetManagerMenuHelper.listFlashcardSets();
                yield true;
            }
            case 3 -> {
                flashcardSetManagerMenuHelper.listFlashcardSets();
                if (flashcardSetManagerMenuHelper.getFlashcardSets().isEmpty()) yield true;

                System.out.print("Geben Sie die Nummer des Sets ein, das bearbeitet werden soll: ");
                int choice = MenuUtils.readMenuSelection() - 1;
                if (choice >= 0 && choice < flashcardSetManagerMenuHelper.getFlashcardSets().size()) {
                    var set = flashcardSetManagerMenuHelper.getFlashcardSets().get(choice);
                    var flashcardManager = new FlashcardManagerMenuHelper(set, flashcardSetManagerMenuHelper.getFlashcardSets());
                    new FlashcardManagerMenu(flashcardManager).start();
                } else {
                    System.out.println("Ungültige Auswahl!");
                }
                yield true;
            }
            case 4 -> {
                flashcardSetManagerMenuHelper.listFlashcardSets();
                if (flashcardSetManagerMenuHelper.getFlashcardSets().isEmpty()) yield true;

                System.out.print("Geben Sie die Nummer des Sets ein, das gelöscht werden soll: ");
                int choice = MenuUtils.readMenuSelection() - 1; // Liste beginnt bei 1
                flashcardSetManagerMenuHelper.deleteFlashcardSet(choice);
                yield true;
            }
            default -> {
                System.out.println("Ungültige Eingabe! Bitte wählen Sie eine gültige Option.");
                yield true;
            }
        };
    }
}