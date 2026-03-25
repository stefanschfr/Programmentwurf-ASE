package src.main.java.de.sagaweschaefer.flashcard.menu.flashcardcreation;

import src.main.java.de.sagaweschaefer.flashcard.menu.Menu;
import src.main.java.de.sagaweschaefer.flashcard.menu.MenuItem;

public class FlashcardCreationMenu {
    private final Menu menu;
    private final FlashcardCreationMenuHelper helper;

    public FlashcardCreationMenu(FlashcardCreationMenuHelper helper) {
        this.helper = helper;
        this.menu = new Menu("Welchen Fragentyp möchten Sie hinzufügen?");
        setupMenu();
    }

    private void setupMenu() {
        menu.addItem(1, new MenuItem("Freitext", helper::addFreeTextFlashcard, true));
        menu.addItem(2, new MenuItem("Multiple Choice", helper::addMultipleChoiceFlashcard, true));
        menu.addItem(3, new MenuItem("Wahr/Falsch", helper::addTrueFalseFlashcard, true));
        menu.addItem(4, new MenuItem("Numerisch", helper::addNumericFlashcard, true));
        menu.addItem(0, new MenuItem("Abbrechen", () -> {}, true));
    }

    public void start() {
        menu.start();
    }
}
