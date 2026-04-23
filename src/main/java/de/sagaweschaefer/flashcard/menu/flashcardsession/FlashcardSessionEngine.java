package de.sagaweschaefer.flashcard.menu.flashcardsession;

import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;
import de.sagaweschaefer.flashcard.model.SessionResult;
import de.sagaweschaefer.flashcard.util.JsonStorage;
import de.sagaweschaefer.flashcard.util.MenuUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FlashcardSessionEngine {
    private static final long EXAM_TIME_LIMIT_MILLIS = 10 * 60 * 1000; // 10 Minuten

    private final JsonStorage storage;

    public FlashcardSessionEngine(JsonStorage storage) {
        this.storage = storage;
    }

    public void runSession(List<Flashcard> cardsToUse, String sessionName) {
        if (cardsToUse.isEmpty()) {
            System.out.println("Keine Karten zum Lernen verfügbar.");
            return;
        }

        List<Flashcard> cards = new ArrayList<>(cardsToUse);
        Collections.shuffle(cards);

        Map<String, FlashcardStatistics> statisticsMap = storage.loadStatistics();
        long startTime = System.currentTimeMillis();
        int correctCount = 0;

        System.out.println("\n--- Session gestartet: " + sessionName + " ---");
        for (Flashcard card : cards) {
            if (processCardInSession(card, statisticsMap)) {
                correctCount++;
            }
        }

        storage.saveStatistics(statisticsMap);

        long duration = System.currentTimeMillis() - startTime;
        saveSessionResult(sessionName, correctCount, cards.size(), duration);
        FlashcardSessionStatistics.displaySessionResult(correctCount, cards.size(), duration);
    }

    private boolean processCardInSession(Flashcard card, Map<String, FlashcardStatistics> statisticsMap) {
        FlashcardStatistics stats = statisticsMap.computeIfAbsent(card.getId(), FlashcardStatistics::new);
        boolean wasDue = stats.isDue();

        if (FlashcardQuestionHelper.askQuestion(card)) {
            System.out.println("Richtig!");
            stats.incrementCorrect();

            displayRatingOptions(wasDue);
            int rating = MenuUtils.promptForInt("Deine Wahl: ");
            stats.applyRating(rating, wasDue);
            return true;
        } else {
            System.out.println("Falsch! Die richtige Antwort war: " + card.getCorrectAnswerDisplay());
            stats.incrementWrong();
            return false;
        }
    }

    private void displayRatingOptions(boolean wasDue) {
        System.out.println("Wie gut konntest du die Frage beantworten?");
        System.out.println("1. Nicht so gut (Level -1)");
        System.out.println("2. Ganz ok (Level bleibt)");
        if (wasDue) {
            System.out.println("3. Sehr gut (Level +1)");
        }
    }

    private void saveSessionResult(String sessionName, int correctCount, int totalCount, long durationMillis) {
        List<SessionResult> results = storage.loadSessionResults();
        results.add(new SessionResult(sessionName, correctCount, totalCount, durationMillis));
        storage.saveSessionResults(results);
    }

    private void saveExamResult(String sessionName, int correctCount, int totalCount, long durationMillis) {
        List<SessionResult> results = storage.loadExamResults();
        results.add(new SessionResult(sessionName, correctCount, totalCount, durationMillis));
        storage.saveExamResults(results);
    }

    public void runExamSession(List<Flashcard> examCards, String setName) {
        if (examCards.isEmpty()) {
            System.out.println("Keine Karten für die Prüfung verfügbar.");
            return;
        }

        long startTime = System.currentTimeMillis();
        int correctCount = 0;
        int totalQuestions = examCards.size();
        Map<String, FlashcardStatistics> statisticsMap = storage.loadStatistics();

        System.out.println("\n--- Prüfung gestartet: " + setName + " ---");
        System.out.println("Anzahl Fragen: " + totalQuestions);
        System.out.println("Zeitlimit: 10 Minuten");

        for (int i = 0; i < examCards.size(); i++) {
            long remainingTime = EXAM_TIME_LIMIT_MILLIS - (System.currentTimeMillis() - startTime);
            if (remainingTime <= 0) {
                System.out.println("\n!!! ZEIT ABGELAUFEN !!!");
                break;
            }

            Flashcard card = examCards.get(i);
            System.out.println("\nFrage " + (i + 1) + " von " + totalQuestions);
            System.out.println("Verbleibende Zeit: " + FlashcardSessionStatistics.formatTime(remainingTime));

            if (processCardInExam(card, statisticsMap)) {
                correctCount++;
            }
        }

        storage.saveStatistics(statisticsMap);
        System.out.println("\n--- Prüfung beendet ---");
        long duration = System.currentTimeMillis() - startTime;
        saveExamResult(setName, correctCount, totalQuestions, duration);
        FlashcardSessionStatistics.displaySessionResult(correctCount, totalQuestions, duration);
    }

    private boolean processCardInExam(Flashcard card, Map<String, FlashcardStatistics> statisticsMap) {
        FlashcardStatistics stats = statisticsMap.computeIfAbsent(card.getId(), FlashcardStatistics::new);
        if (FlashcardQuestionHelper.askQuestion(card)) {
            System.out.println("Richtig!");
            stats.incrementCorrect();
            return true;
        } else {
            System.out.println("Falsch! Die richtige Antwort war: " + card.getCorrectAnswerDisplay());
            stats.incrementWrong();
            return false;
        }
    }
}
