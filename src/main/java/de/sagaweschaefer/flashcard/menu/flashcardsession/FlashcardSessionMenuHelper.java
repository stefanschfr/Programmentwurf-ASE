package de.sagaweschaefer.flashcard.menu.flashcardsession;

import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;
import de.sagaweschaefer.flashcard.util.JsonStorage;
import de.sagaweschaefer.flashcard.util.MenuUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FlashcardSessionMenuHelper {
    private final JsonStorage storage;
    private final FlashcardSessionEngine engine;
    private List<FlashcardSet> flashcardSets;

    public FlashcardSessionMenuHelper(JsonStorage storage) {
        this.storage = storage;
        this.engine = new FlashcardSessionEngine(storage);
        refreshFlashcardSets();
    }

    private void refreshFlashcardSets() {
        this.flashcardSets = storage.loadFlashcardSets();
    }

    public void startSession() {
        refreshFlashcardSets();
        if (flashcardSets.isEmpty()) {
            System.out.println("Es sind keine Lernkartensets verfügbar.");
            return;
        }

        MenuUtils.displayFlashcardSets(flashcardSets, "Verfügbare Lernkartensets");
        FlashcardSet set = MenuUtils.selectFromList(flashcardSets, "Wähle ein Lernkartenset (Nummer): ");
        if (set == null) return;

        Map<String, FlashcardStatistics> statisticsMap = storage.loadStatistics();
        List<Flashcard> cardsToLearn = new ArrayList<>();
        for (Flashcard card : set.getFlashcards()) {
            FlashcardStatistics stats = statisticsMap.get(card.getId());
            if (stats == null || stats.getLevel() < 6) {
                cardsToLearn.add(card);
            }
        }

        if (cardsToLearn.isEmpty()) {
            System.out.println("Alle Karten in diesem Set sind bereits vollständig gelernt (Stufe 6)!");
            return;
        }

        engine.runSession(cardsToLearn, set.getName());
    }

    public void startWrongAnswersSession() {
        refreshFlashcardSets();
        if (flashcardSets.isEmpty()) {
            System.out.println("Es sind keine Lernkartensets verfügbar.");
            return;
        }

        Map<String, FlashcardStatistics> statisticsMap = storage.loadStatistics();
        List<Flashcard> wrongAnswers = new ArrayList<>();

        for (FlashcardSet set : flashcardSets) {
            for (Flashcard card : set.getFlashcards()) {
                FlashcardStatistics stats = statisticsMap.get(card.getId());
                if (stats != null && stats.getLevel() == 0) {
                    wrongAnswers.add(card);
                }
            }
        }

        if (wrongAnswers.isEmpty()) {
            System.out.println("Es sind keine falsch beantworteten Fragen (Stufe 0) vorhanden.");
            return;
        }

        engine.runSession(wrongAnswers, "Falsch beantwortete Fragen (Stufe 0)");
    }

    public void startDueCardsSession() {
        refreshFlashcardSets();
        if (flashcardSets.isEmpty()) {
            System.out.println("Es sind keine Lernkartensets verfügbar.");
            return;
        }

        Map<String, FlashcardStatistics> statisticsMap = storage.loadStatistics();
        List<Flashcard> dueCards = new ArrayList<>();

        for (FlashcardSet set : flashcardSets) {
            for (Flashcard card : set.getFlashcards()) {
                FlashcardStatistics stats = statisticsMap.get(card.getId());
                if (stats == null || stats.isDue()) {
                    dueCards.add(card);
                }
            }
        }

        if (dueCards.isEmpty()) {
            System.out.println("Es sind momentan keine Lernkarten fällig.");
            return;
        }

        engine.runSession(dueCards, "Fällige Lernkarten");
    }

    public void startExamMode() {
        refreshFlashcardSets();
        if (flashcardSets.isEmpty()) {
            System.out.println("Es sind keine Lernkartensets verfügbar.");
            return;
        }

        MenuUtils.displayFlashcardSets(flashcardSets, "Verfügbare Lernkartensets");
        FlashcardSet set = MenuUtils.selectFromList(flashcardSets, "Wähle ein Lernkartenset für die Prüfung (Nummer): ");
        if (set == null) return;

        List<Flashcard> allCards = new ArrayList<>(set.getFlashcards());

        if (allCards.size() < 10) {
            System.out.println("Das gewählte Set enthält nur " + allCards.size() + " Karten. Für den Prüfungsmodus sind mindestens 10 Karten erforderlich.");
            return;
        }

        Collections.shuffle(allCards);
        List<Flashcard> examCards = allCards.subList(0, 10);

        engine.runExamSession(examCards, set.getName());
    }
}
