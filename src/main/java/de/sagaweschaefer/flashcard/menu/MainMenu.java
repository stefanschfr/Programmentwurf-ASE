package de.sagaweschaefer.flashcard.menu;

import de.sagaweschaefer.flashcard.menu.flashcardsession.FlashcardSessionMenu;
import de.sagaweschaefer.flashcard.menu.flashcardsetmanager.FlashcardSetManagerMenu;
import de.sagaweschaefer.flashcard.menu.statistics.StatisticsMenu;
import de.sagaweschaefer.flashcard.util.AppScanner;
import de.sagaweschaefer.flashcard.util.FlashcardStorage;

public class MainMenu {
    private final Menu menu;
    private final FlashcardStorage storage = new de.sagaweschaefer.flashcard.util.JsonStorage();
    private final FlashcardSetManagerMenu flashcardSetManagerMenu = new FlashcardSetManagerMenu(storage);
    private final FlashcardSessionMenu flashcardSessionMenu = new FlashcardSessionMenu(storage);
    private final StatisticsMenu statisticsMenu = new StatisticsMenu(storage);

    public MainMenu() {
        this.menu = new Menu("Hauptmenü");
        setupMenu();
    }

    public static void main(String[] args) {
        new MainMenu().start();
        AppScanner.SCANNER.close();
    }

    private void setupMenu() {
        menu.addItem(1, new MenuItem("Flashcard Set Manager öffnen", flashcardSetManagerMenu::start));
        menu.addItem(2, new MenuItem("Lernsession starten", flashcardSessionMenu::start));
        menu.addItem(3, new MenuItem("Statistiken anzeigen", statisticsMenu::start));
        menu.addItem(0, new MenuItem("Programm beenden", () -> System.out.println("Programm wird beendet. Auf Wiedersehen!"), true));
    }

    public void start() {
        menu.start();
    }
}