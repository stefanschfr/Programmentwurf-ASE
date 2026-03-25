package src.main.java.de.sagaweschaefer.flashcard.menu;

public class FlashcardManagerMenu extends Menu {
    private final FlashcardManagerMenuHelper flashcardManagerMenuHelper;

    public FlashcardManagerMenu(FlashcardManagerMenuHelper flashcardManagerMenuHelper) {
        this.flashcardManagerMenuHelper = flashcardManagerMenuHelper;
    }

    @Override
    protected void showMenu() {
        System.out.println("\n=== Flashcard Manager für Set: " + flashcardManagerMenuHelper.getFlashcardSet().getName() + " ===");
        System.out.println("1. Neue Frage hinzufügen");
        System.out.println("2. Alle Fragen anzeigen");
        System.out.println("3. Frage löschen");
        System.out.println("0. Zurück");
    }

    @Override
    protected boolean handleSelection(int selection) {
        return switch (selection) {
            case 0 -> false;
            case 1 -> {
                flashcardManagerMenuHelper.addFlashcard();
                yield true;
            }
            case 2 -> {
                flashcardManagerMenuHelper.listFlashcards();
                yield true;
            }
            case 3 -> {
                flashcardManagerMenuHelper.deleteFlashcard();
                yield true;
            }
            default -> {
                System.out.println("Ungültige Eingabe!");
                yield true;
            }
        };
    }
}
