package src.main.java.de.sagaweschaefer.flashcard.menu;

public class FlashcardManagerMenu {
    private final Menu menu;
    private final FlashcardManagerMenuHelper flashcardManagerMenuHelper;

    public FlashcardManagerMenu(FlashcardManagerMenuHelper flashcardManagerMenuHelper) {
        this.flashcardManagerMenuHelper = flashcardManagerMenuHelper;
        this.menu = new Menu("Flashcard Manager für Set: " + flashcardManagerMenuHelper.getFlashcardSet().getName());
        setupMenu();
    }

    private void setupMenu() {
        menu.addItem(1, new MenuItem("Neue Frage hinzufügen", flashcardManagerMenuHelper::addFlashcard));
        menu.addItem(2, new MenuItem("Alle Fragen anzeigen", flashcardManagerMenuHelper::listFlashcards));
        menu.addItem(3, new MenuItem("Frage löschen", flashcardManagerMenuHelper::deleteFlashcard));
        menu.addItem(0, new MenuItem("Zurück", () -> {}, true));
    }

    public void start() {
        menu.start();
    }
}
