package src.main.java.de.sagaweschaefer.flashcard.menu;


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
    }

    @Override
    protected boolean handleSelection(int selection) {
        return switch (selection) {
            case 0 -> false;
            case 1 -> {
                flashcardSetManagerMenuHelper.addFlashcardSet();
                yield true;
            }
            case 2 -> {
                flashcardSetManagerMenuHelper.listFlashcardSets();
                yield true;
            }
            case 3 -> {
                flashcardSetManagerMenuHelper.editFlashcardSet();
                yield true;
            }
            case 4 -> {
                flashcardSetManagerMenuHelper.deleteFlashcardSet();
                yield true;
            }
            default -> {
                System.out.println("Ungültige Eingabe! Bitte wählen Sie eine gültige Option.");
                yield true;
            }
        };
    }
}