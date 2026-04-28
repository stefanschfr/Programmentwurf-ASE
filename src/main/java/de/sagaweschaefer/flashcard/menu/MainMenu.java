package de.sagaweschaefer.flashcard.menu;

import de.sagaweschaefer.flashcard.configuration.ApplicationContext;
import de.sagaweschaefer.flashcard.menu.flashcardsession.FlashcardSessionMenu;
import de.sagaweschaefer.flashcard.menu.flashcardsetmanager.FlashcardSetManagerMenu;
import de.sagaweschaefer.flashcard.menu.statistics.StatisticsMenu;
import de.sagaweschaefer.flashcard.util.AppScanner;

public class MainMenu {
    private final Menu menu;
    private final FlashcardSetManagerMenu flashcardSetManagerMenu;
    private final FlashcardSessionMenu flashcardSessionMenu;
    private final StatisticsMenu statisticsMenu;

    public MainMenu() {
        ApplicationContext applicationContext = new ApplicationContext();
        this.menu = new Menu("Hauptmenü");
        this.flashcardSetManagerMenu = applicationContext.createFlashcardSetManagerMenu();
        this.flashcardSessionMenu = applicationContext.createFlashcardSessionMenu();
        this.statisticsMenu = applicationContext.createStatisticsMenu();
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