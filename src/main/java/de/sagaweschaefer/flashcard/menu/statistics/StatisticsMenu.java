package de.sagaweschaefer.flashcard.menu.statistics;

import de.sagaweschaefer.flashcard.menu.Menu;
import de.sagaweschaefer.flashcard.menu.MenuItem;
import de.sagaweschaefer.flashcard.menu.flashcardsetmanager.FlashcardSetManagerMenuHelper;

public class StatisticsMenu {
    private final Menu menu;
    private final StatisticsMenuHelper statisticsMenuHelper = new StatisticsMenuHelper();

    public StatisticsMenu() {
        this.menu = new Menu("Statistiken");
        setupMenu();
    }

    private void setupMenu() {
        menu.addItem(1, new MenuItem("Gesamtstatistik anzeigen", statisticsMenuHelper::showGeneralStatistics));
        menu.addItem(2, new MenuItem("Statistik nach Lernkartenset anzeigen", statisticsMenuHelper::showSetStatistics));
        menu.addItem(3, new MenuItem("Statistik für einzelne Frage anzeigen", statisticsMenuHelper::showCardStatistics));
        menu.addItem(0, new MenuItem("Zurück zum Hauptmenü", () -> {}, true));
    }



    public void start() {
        menu.start();
    }
}
