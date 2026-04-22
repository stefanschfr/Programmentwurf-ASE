package de.sagaweschaefer.flashcard.menu.flashcardcreation;

import de.sagaweschaefer.flashcard.menu.Menu;
import de.sagaweschaefer.flashcard.menu.MenuItem;

public class FlashcardCreationMenu {
    private final Menu menu;
    private final FlashcardCreationMenuHelper flashcardCreationMenuHelper;

    public FlashcardCreationMenu(FlashcardCreationMenuHelper flashcardCreationMenuHelper) {
        this.flashcardCreationMenuHelper = flashcardCreationMenuHelper;
        this.menu = new Menu("Welchen Fragentyp möchten Sie hinzufügen?");
        setupMenu();
    }

    private void setupMenu() {
        menu.addItem(1, new MenuItem("Freitext", flashcardCreationMenuHelper::addFreeTextFlashcard, true));
        menu.addItem(2, new MenuItem("Multiple Choice", flashcardCreationMenuHelper::addMultipleChoiceFlashcard, true));
        menu.addItem(3, new MenuItem("Wahr/Falsch", flashcardCreationMenuHelper::addTrueFalseFlashcard, true));
        menu.addItem(4, new MenuItem("Numerisch", flashcardCreationMenuHelper::addNumericFlashcard, true));
        menu.addItem(0, new MenuItem("Abbrechen", () -> {}, true));
    }

    public void start() {
        menu.start();
    }
}
