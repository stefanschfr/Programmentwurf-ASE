package src.main.java.de.sagaweschaefer.flashcard.menu.flashcardsession;

import src.main.java.de.sagaweschaefer.flashcard.menu.Menu;
import src.main.java.de.sagaweschaefer.flashcard.menu.MenuItem;

public class FlashcardSessionMenu {
    private final Menu menu;
    private final FlashcardSessionMenuHelper helper = new FlashcardSessionMenuHelper();

    public FlashcardSessionMenu() {
        this.menu = new Menu("Lernsession");
        setupMenu();
    }

    private void setupMenu() {
        menu.addItem(1, new MenuItem("Neue Lernsession starten", helper::startSession));
        menu.addItem(2, new MenuItem("Session mit falsch beantworteten Fragen starten", helper::startWrongAnswersSession));
        menu.addItem(0, new MenuItem("Zurück zum Hauptmenü", () -> {}, true));
    }

    public void start() {
        menu.start();
    }
}
