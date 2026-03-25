package src.main.java.de.sagaweschaefer.flashcard.menu;

import src.main.java.de.sagaweschaefer.flashcard.menu.flashcardsession.FlashcardSessionMenu;
import src.main.java.de.sagaweschaefer.flashcard.menu.flashcardsetmanager.FlashcardSetManagerMenu;
import src.main.java.de.sagaweschaefer.flashcard.util.AppScanner;

public class MainMenu {

    private final Menu menu;
    private final FlashcardSetManagerMenu flashcardSetManagerMenu = new FlashcardSetManagerMenu();
    private final FlashcardSessionMenu flashcardSessionMenu = new FlashcardSessionMenu();

    public MainMenu() {
        this.menu = new Menu("Hauptmenü");
        setupMenu();
    }

    private void setupMenu() {
        menu.addItem(1, new MenuItem("Flashcard Set Manager öffnen", flashcardSetManagerMenu::start));
        menu.addItem(2, new MenuItem("Lernsession starten", flashcardSessionMenu::start));
        menu.addItem(0, new MenuItem("Programm beenden", () -> System.out.println("Programm wird beendet. Auf Wiedersehen!"), true));
    }

    public void start() {
        menu.start();
    }

    public static void main(String[] args) {
        new MainMenu().start();
        AppScanner.SCANNER.close();
    }
}