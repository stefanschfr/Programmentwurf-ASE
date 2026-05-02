package de.sagaweschaefer.flashcard.menu;

import de.sagaweschaefer.flashcard.application.ApplicationContext;
import de.sagaweschaefer.flashcard.menu.dailyplan.DailyPlanMenu;
import de.sagaweschaefer.flashcard.menu.flashcardsession.FlashcardSessionMenu;
import de.sagaweschaefer.flashcard.menu.flashcardsetmanager.FlashcardSetManagerMenu;
import de.sagaweschaefer.flashcard.menu.statistics.StatisticsMenu;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;
import de.sagaweschaefer.flashcard.model.SessionResult;
import de.sagaweschaefer.flashcard.util.AppScanner;
import de.sagaweschaefer.flashcard.util.JsonStorage;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class MainMenu {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final Menu menu;
    private final JsonStorage storage = new JsonStorage();
    private final ApplicationContext applicationContext = new ApplicationContext();
    private final FlashcardSetManagerMenu flashcardSetManagerMenu = new FlashcardSetManagerMenu(storage);
    private final FlashcardSessionMenu flashcardSessionMenu = new FlashcardSessionMenu(storage);
    private final StatisticsMenu statisticsMenu = new StatisticsMenu(storage);
    private final DailyPlanMenu dailyPlanMenu = new DailyPlanMenu(
            applicationContext.getSetDailyGoalUseCase(),
            applicationContext.getGetDailyProgressUseCase(),
            applicationContext.getRecommendDailyCardsUseCase(),
            applicationContext.getDailyLearningPlanService());

    public MainMenu() {
        this.menu = new Menu("Hauptmenü");
        this.menu.setBeforeDisplayHook(this::showDashboard);
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
        menu.addItem(4, new MenuItem("Täglicher Lernplan", dailyPlanMenu::start));
        menu.addItem(0, new MenuItem("Programm beenden", () -> System.out.println("Programm wird beendet. Auf Wiedersehen!"), true));
    }

    public void start() {
        menu.start();
    }

    private void showDashboard() {
        List<FlashcardSet> sets = storage.loadFlashcardSets();
        Map<String, FlashcardStatistics> stats = storage.loadStatistics();
        List<SessionResult> sessions = storage.loadSessionResults();
        List<SessionResult> exams = storage.loadExamResults();

        int totalCards = sets.stream().mapToInt(s -> s.getFlashcards().size()).sum();
        int dueCards = (int) stats.values().stream().filter(FlashcardStatistics::isDue).count();

        System.out.println("\n----------------------------------------");
        System.out.println("Flashcard-App Dashboard");
        System.out.println("Sets: " + sets.size() + " | Karten: " + totalCards + " | Fällig: " + dueCards);
        System.out.println("Sessions: " + sessions.size() + " | Prüfungen: " + exams.size());

        if (!sessions.isEmpty()) {
            SessionResult last = sessions.getLast();
            System.out.println("Letzte Session: " + last.getSessionName() + " am "
                    + last.getTimestamp().format(DATE_TIME_FORMATTER));
        }
        System.out.println("----------------------------------------");
    }
}