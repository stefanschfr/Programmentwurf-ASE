package src.main.java.de.sagaweschaefer.flashcard.menu;


public class FlashcardSetManagerMenu {

    private final Menu menu;
    private final FlashcardSetManagerMenuHelper flashcardSetManagerMenuHelper = new FlashcardSetManagerMenuHelper();

    public FlashcardSetManagerMenu() {
        this.menu = new Menu("Flashcard Set Manager");
        setupMenu();
    }

    private void setupMenu() {
        menu.addItem(1, new MenuItem("Neues Lernkarten-Set erstellen", flashcardSetManagerMenuHelper::addFlashcardSet));
        menu.addItem(2, new MenuItem("Alle Lernkarten-Sets anzeigen", flashcardSetManagerMenuHelper::listFlashcardSets));
        menu.addItem(3, new MenuItem("Lernkartenset bearbeiten", flashcardSetManagerMenuHelper::editFlashcardSet));
        menu.addItem(4, new MenuItem("Lernkartenset löschen", flashcardSetManagerMenuHelper::deleteFlashcardSet));
        menu.addItem(0, new MenuItem("Zurück zum Hauptmenü", () -> {}, true));
    }

    public void start() {
        menu.start();
    }
}