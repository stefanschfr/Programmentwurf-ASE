package de.sagaweschaefer.flashcard.menu.statistics;

import de.sagaweschaefer.flashcard.menu.Menu;
import de.sagaweschaefer.flashcard.menu.MenuItem;
import de.sagaweschaefer.flashcard.util.JsonStorage;

public class StatisticsMenu {
    private final Menu menu;
    private final StatisticsMenuHelper statisticsMenuHelper;

    public StatisticsMenu(JsonStorage storage) {
        this.statisticsMenuHelper = new StatisticsMenuHelper(storage);
        this.menu = new Menu("Statistiken");
        setupMenu();
    }

    private void setupMenu() {
        menu.addItem(1, new MenuItem("Gesamtstatistik anzeigen", statisticsMenuHelper::showGeneralStatistics));
        menu.addItem(2, new MenuItem("Statistik nach Lernkartenset anzeigen", statisticsMenuHelper::showSetStatistics));
        menu.addItem(3, new MenuItem("Statistik für einzelne Frage anzeigen", statisticsMenuHelper::showCardStatistics));
        menu.addItem(4, new MenuItem("Ergebnisse der letzten Lernsessions anzeigen", statisticsMenuHelper::showLastSessionResults));
        menu.addItem(5, new MenuItem("Ergebnisse der letzten Prüfungen anzeigen", statisticsMenuHelper::showLastExamResults));
        menu.addItem(0, new MenuItem("Zurück zum Hauptmenü", () -> {
        }, true));
    }


    public void start() {
        menu.start();
    }
}
