package de.sagaweschaefer.flashcard.menu.flashcardsession;

import de.sagaweschaefer.flashcard.menu.Menu;
import de.sagaweschaefer.flashcard.menu.MenuItem;
import de.sagaweschaefer.flashcard.util.JsonStorage;

public class FlashcardSessionMenu {
    private final Menu menu;
    private final FlashcardSessionMenuHelper helper;

    public FlashcardSessionMenu(JsonStorage storage) {
        this.helper = new FlashcardSessionMenuHelper(storage);
        this.menu = new Menu("Lernsession");
        setupMenu();
    }

    private void setupMenu() {
        menu.addItem(1, new MenuItem("Neue Lernsession starten", helper::startSession));
        menu.addItem(2, new MenuItem("Session mit fälligen Lernkarten starten", helper::startDueCardsSession));
        menu.addItem(3, new MenuItem("Session mit falsch beantworteten Fragen starten", helper::startWrongAnswersSession));
        menu.addItem(4, new MenuItem("Prüfungsmodus starten (10 Fragen, 10 Min)", helper::startExamMode));
        menu.addItem(0, new MenuItem("Zurück zum Hauptmenü", () -> {
        }, true));
    }

    public void start() {
        menu.start();
    }
}
