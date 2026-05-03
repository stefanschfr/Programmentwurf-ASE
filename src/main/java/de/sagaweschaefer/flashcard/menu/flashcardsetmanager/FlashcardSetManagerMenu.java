package de.sagaweschaefer.flashcard.menu.flashcardsetmanager;


import de.sagaweschaefer.flashcard.menu.Menu;
import de.sagaweschaefer.flashcard.menu.MenuItem;
import de.sagaweschaefer.flashcard.util.JsonStorage;

public class FlashcardSetManagerMenu {
    private final Menu menu;
    private final FlashcardSetManagerMenuHelper flashcardSetManagerMenuHelper;

    public FlashcardSetManagerMenu(JsonStorage storage) {
        this.flashcardSetManagerMenuHelper = new FlashcardSetManagerMenuHelper(storage);
        this.menu = new Menu("Flashcard Set Manager");
        setupMenu();
    }

    private void setupMenu() {
        menu.addItem(1, new MenuItem("Neues Lernkarten-Set erstellen", flashcardSetManagerMenuHelper::addFlashcardSet));
        menu.addItem(2, new MenuItem("Alle Lernkarten-Sets anzeigen", flashcardSetManagerMenuHelper::listFlashcardSets));
        menu.addItem(3, new MenuItem("Lernkartenset bearbeiten", flashcardSetManagerMenuHelper::editFlashcardSet));
        menu.addItem(4, new MenuItem("Lernkartenset löschen", flashcardSetManagerMenuHelper::deleteFlashcardSet));
        menu.addItem(5, new MenuItem("Lernkartensets exportieren (JSON)", flashcardSetManagerMenuHelper::exportFlashcardSets));
        menu.addItem(6, new MenuItem("Lernkartensets importieren (JSON)", flashcardSetManagerMenuHelper::importFlashcardSets));
        menu.addItem(0, new MenuItem("Zurück zum Hauptmenü", () -> {
        }, true));
    }

    public void start() {
        menu.start();
    }
}