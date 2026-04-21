package de.sagaweschaefer.flashcard.menu.flashcardsession;

import de.sagaweschaefer.flashcard.model.Flashcard;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;
import de.sagaweschaefer.flashcard.util.JsonStorage;
import de.sagaweschaefer.flashcard.util.MenuUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FlashcardSessionEngine {

    private final JsonStorage storage;

    public FlashcardSessionEngine(JsonStorage storage) {
        this.storage = storage;
    }

    public void runSession(List<Flashcard> cardsToUse, String sessionName) {
        List<Flashcard> cards = new ArrayList<>(cardsToUse);
        if (cards.isEmpty()) {
            System.out.println("Keine Karten zum Lernen verfügbar.");
            return;
        }

        Collections.shuffle(cards);
        int correctCount = 0;
        Map<String, FlashcardStatistics> statisticsMap = storage.loadStatistics();
        boolean statsChanged = false;

        System.out.println("\n--- Session gestartet: " + sessionName + " ---");
        for (Flashcard card : cards) {
            FlashcardStatistics stats = statisticsMap.computeIfAbsent(card.getId(), FlashcardStatistics::new);
            boolean wasDue = stats.isDue();
            if (FlashcardQuestionHelper.askQuestion(card)) {
                System.out.println("Richtig!");
                correctCount++;
                stats.incrementCorrect();
                
                System.out.println("Wie gut konntest du die Frage beantworten?");
                System.out.println("1. Nicht so gut (Level -1)");
                System.out.println("2. Ganz ok (Level bleibt)");
                if (wasDue) {
                    System.out.println("3. Sehr gut (Level +1)");
                }
                
                int rating = MenuUtils.promptForInt("Deine Wahl: ");
                stats.applyRating(rating, wasDue);
                
                statsChanged = true;
            } else {
                System.out.println("Falsch! Die richtige Antwort war: " + FlashcardQuestionHelper.getCorrectAnswerDisplay(card));
                stats.incrementWrong();
                statsChanged = true;
            }
        }

        if (statsChanged) {
            storage.saveStatistics(statisticsMap);
        }
        FlashcardSessionStatistics.displaySessionResult(correctCount, cards.size(), 0);
    }

    public void runExamSession(List<Flashcard> examCards, String setName) {
        long startTime = System.currentTimeMillis();
        long limitMillis = 10 * 60 * 1000; // 10 Minuten
        int correctCount = 0;
        int totalQuestions = examCards.size();
        Map<String, FlashcardStatistics> statisticsMap = storage.loadStatistics();
        boolean statsChanged = false;

        System.out.println("\n--- Prüfung gestartet: " + setName + " ---");
        System.out.println("Anzahl Fragen: " + totalQuestions);
        System.out.println("Zeitlimit: 10 Minuten");

        for (int i = 0; i < examCards.size(); i++) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime;
            
            if (elapsedTime >= limitMillis) {
                System.out.println("\n!!! ZEIT ABGELAUFEN !!!");
                break;
            }

            Flashcard card = examCards.get(i);
            FlashcardStatistics stats = statisticsMap.computeIfAbsent(card.getId(), FlashcardStatistics::new);
            System.out.println("\nFrage " + (i + 1) + " von " + totalQuestions);
            System.out.println("Verbleibende Zeit: " + FlashcardSessionStatistics.formatTime(limitMillis - elapsedTime));

            if (FlashcardQuestionHelper.askQuestion(card)) {
                System.out.println("Richtig!");
                correctCount++;
                stats.incrementCorrect();
                statsChanged = true;
            } else {
                System.out.println("Falsch! Die richtige Antwort war: " + FlashcardQuestionHelper.getCorrectAnswerDisplay(card));
                stats.incrementWrong();
                statsChanged = true;
            }
            
            if (System.currentTimeMillis() - startTime >= limitMillis) {
                System.out.println("\n!!! ZEIT WÄHREND DER LETZTEN FRAGE ABGELAUFEN !!!");
                if (i < totalQuestions - 1) {
                    break;
                }
            }
        }

        if (statsChanged) {
            storage.saveStatistics(statisticsMap);
        }
        System.out.println("\n--- Prüfung beendet ---");
        FlashcardSessionStatistics.displaySessionResult(correctCount, totalQuestions, System.currentTimeMillis() - startTime);
    }
}
