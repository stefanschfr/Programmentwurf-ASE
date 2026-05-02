package de.sagaweschaefer.flashcard.menu.dailyplan;

import de.sagaweschaefer.flashcard.application.usecase.GetDailyProgressUseCase;
import de.sagaweschaefer.flashcard.application.usecase.RecommendDailyCardsUseCase;
import de.sagaweschaefer.flashcard.application.usecase.SetDailyGoalUseCase;
import de.sagaweschaefer.flashcard.domain.service.DailyLearningPlanService;
import de.sagaweschaefer.flashcard.domain.valueobject.DailyGoal;
import de.sagaweschaefer.flashcard.menu.Menu;
import de.sagaweschaefer.flashcard.menu.MenuItem;
import de.sagaweschaefer.flashcard.model.DailyProgress;
import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.util.MenuUtils;

import java.util.List;

/**
 * CLI-Menü für den täglichen Lernplan.
 *
 * <p>Hängt nur von Application-UseCases und einem Domain-Service ab; kein
 * direkter Zugriff auf Adapter/Persistenz.</p>
 */
@SuppressWarnings("unused") // Wird via MainMenu instanziiert und gestartet
public class DailyPlanMenu {

    private static final int PROGRESS_BAR_WIDTH = 30;

    private final SetDailyGoalUseCase setGoalUseCase;
    private final GetDailyProgressUseCase getProgressUseCase;
    private final RecommendDailyCardsUseCase recommendUseCase;
    private final DailyLearningPlanService service;
    private final Menu menu;

    public DailyPlanMenu(SetDailyGoalUseCase setGoalUseCase,
                         GetDailyProgressUseCase getProgressUseCase,
                         RecommendDailyCardsUseCase recommendUseCase,
                         DailyLearningPlanService service) {
        this.setGoalUseCase = setGoalUseCase;
        this.getProgressUseCase = getProgressUseCase;
        this.recommendUseCase = recommendUseCase;
        this.service = service;
        this.menu = new Menu("Täglicher Lernplan");
        setupMenu();
    }

    private void setupMenu() {
        menu.addItem(1, new MenuItem("Tagesfortschritt anzeigen", this::showProgress));
        menu.addItem(2, new MenuItem("Tagesziel setzen", this::setGoal));
        menu.addItem(3, new MenuItem("Empfohlene Karten anzeigen", this::showRecommendations));
        menu.addItem(4, new MenuItem("Karte als gelernt vermerken", this::recordCard));
        menu.addItem(0, new MenuItem("Zurück", () -> { /* schließt Menü */ }, true));
    }

    public void start() {
        menu.start();
    }

    // --- Aktionen ---

    private void showProgress() {
        GetDailyProgressUseCase.Result result = getProgressUseCase.execute();
        DailyGoal goal = result.getGoal();
        DailyProgress progress = result.getProgress();

        System.out.println("\n--- Tagesfortschritt (" + progress.getDate() + ") ---");
        System.out.println("Ziel: " + goal);
        System.out.println("Gelernt: " + progress.getLearnedCards() + " / " + progress.getGoalCards());
        System.out.println("Davon richtig: " + progress.getCorrectCards());
        System.out.println("Fortschritt: " + renderProgressBar(progress.getProgressRatio()));
        if (progress.isGoalReached()) {
            System.out.println("Tagesziel erreicht. Sehr gut!");
        }
    }

    private void setGoal() {
        int cards = MenuUtils.promptForInt("Wie viele Karten möchtest du täglich lernen? ");
        try {
            DailyGoal goal = setGoalUseCase.execute(cards);
            System.out.println("Tagesziel gesetzt: " + goal);
        } catch (IllegalArgumentException e) {
            System.out.println("Ungültiges Tagesziel: " + e.getMessage());
        }
    }

    private void showRecommendations() {
        List<Flashcard> cards = recommendUseCase.execute();
        if (cards.isEmpty()) {
            System.out.println("Keine offenen Karten für heute (Ziel erreicht oder keine Karten verfügbar).");
            return;
        }
        System.out.println("\n--- Empfehlung für heute (" + cards.size() + " Karten) ---");
        for (int i = 0; i < cards.size(); i++) {
            Flashcard c = cards.get(i);
            System.out.printf("%2d. [%s] %s%n",
                    (i + 1),
                    c.getQuestionType().getDisplayName(),
                    c.getQuestion());
        }
    }

    private void recordCard() {
        String input = MenuUtils.promptForString("War die Karte korrekt? (j/n): ").trim().toLowerCase();
        boolean correct = input.startsWith("j");
        DailyProgress updated = service.recordCardLearned(correct);
        System.out.println("Vermerkt. Aktueller Stand: "
                + updated.getLearnedCards() + "/" + updated.getGoalCards()
                + " (richtig: " + updated.getCorrectCards() + ")");
    }

    /** Rendert einen ASCII-Fortschrittsbalken für ein Verhältnis 0..1. */
    static String renderProgressBar(double ratio) {
        double clamped = Math.max(0.0, Math.min(1.0, ratio));
        int filled = (int) Math.round(clamped * PROGRESS_BAR_WIDTH);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < PROGRESS_BAR_WIDTH; i++) {
            sb.append(i < filled ? '#' : '-');
        }
        sb.append("] ");
        sb.append(Math.round(clamped * 100));
        sb.append('%');
        return sb.toString();
    }
}


