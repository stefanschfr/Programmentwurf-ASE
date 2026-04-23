package de.sagaweschaefer.flashcard.menu.statistics;

import de.sagaweschaefer.flashcard.menu.flashcardsession.FlashcardSessionStatistics;
import de.sagaweschaefer.flashcard.model.*;
import de.sagaweschaefer.flashcard.util.JsonStorage;
import de.sagaweschaefer.flashcard.util.MenuUtils;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class StatisticsMenuHelper {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final JsonStorage storage;

    public StatisticsMenuHelper(JsonStorage storage) {
        this.storage = storage;
    }

    public void showGeneralStatistics() {
        List<FlashcardSet> sets = storage.loadFlashcardSets();
        Map<String, FlashcardStatistics> statsMap = storage.loadStatistics();
        GeneralStatistics stats = GeneralStatistics.calculate(sets, statsMap);

        printHeader("Gesamtstatistik");
        System.out.println("Gesamte Anzahl an Lernsets: " + stats.getTotalSets());
        printBaseStatistics(stats);

        System.out.println("Noch nie beantwortete Karten: " + stats.getNeverAnswered());
        System.out.printf("Durchschnittliches Level: %.2f\n", stats.getAverageLevel());

        printLevelDistribution(stats.getLevelDistribution());

        if (!"N/A".equals(stats.getMostFrequentQuestion())) {
            System.out.println("\nMeistgeübte Karte: \"" + stats.getMostFrequentQuestion() + "\" (" + stats.getMostFrequentCount() + " Versuche)");
        }
    }

    public void showSetStatistics() {
        List<FlashcardSet> sets = storage.loadFlashcardSets();
        if (sets.isEmpty()) {
            System.out.println("Keine Lernsets vorhanden.");
            return;
        }

        MenuUtils.displayFlashcardSets(sets, "Statistik nach Set");
        FlashcardSet selectedSet = MenuUtils.selectFromList(sets, "Bitte wählen Sie ein Set aus (0 zum Abbrechen): ");
        if (selectedSet == null) return;

        Map<String, FlashcardStatistics> statsMap = storage.loadStatistics();
        FlashcardSetStatistics stats = FlashcardSetStatistics.calculate(selectedSet, statsMap);

        printHeader("Statistik für Set: " + stats.getSetName());
        printBaseStatistics(stats);

        printLevelDistribution(stats.getLevelDistribution());
    }

    private void printBaseStatistics(BaseStatistics stats) {
        System.out.println("Anzahl Fragen: " + stats.getTotalQuestions());
        System.out.println("Richtig beantwortet: " + stats.getTotalCorrect());
        System.out.println("Falsch beantwortet: " + stats.getTotalWrong());
        System.out.printf("Erfolgsquote: %.2f%%\n", stats.getCorrectPercentage());
        printSeparator();
        System.out.println("Fällige Karten: " + stats.getDueCards());
    }

    public void showCardStatistics() {
        List<FlashcardSet> sets = storage.loadFlashcardSets();
        if (sets.isEmpty()) {
            System.out.println("Keine Lernsets vorhanden.");
            return;
        }

        MenuUtils.displayFlashcardSets(sets, "Einzelspezifische Statistik: Set wählen");
        FlashcardSet selectedSet = MenuUtils.selectFromList(sets, "Bitte wählen Sie ein Set aus (0 zum Abbrechen): ");
        if (selectedSet == null) return;

        List<Flashcard> cards = selectedSet.getFlashcards();
        if (cards.isEmpty()) {
            System.out.println("Dieses Set enthält keine Karten.");
            return;
        }

        System.out.println("\n--- Karten im Set: " + selectedSet.getName() + " ---");
        for (int i = 0; i < cards.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, cards.get(i).getQuestion());
        }

        Flashcard selectedCard = MenuUtils.selectFromList(cards, "Bitte wählen Sie eine Karte aus (0 zum Abbrechen): ");
        if (selectedCard == null) return;

        Map<String, FlashcardStatistics> statsMap = storage.loadStatistics();
        FlashcardStatistics stats = statsMap.get(selectedCard.getId());

        printHeader("Statistik für Frage");
        System.out.println("Frage: " + selectedCard.getQuestion());
        System.out.println("Typ: " + selectedCard.getQuestionType());

        if (stats != null) {
            int totalAnswers = stats.getCorrectCount() + stats.getWrongCount();
            double correctPercentage = totalAnswers > 0 ? (double) stats.getCorrectCount() / totalAnswers * 100 : 0;

            System.out.println("Richtig beantwortet: " + stats.getCorrectCount());
            System.out.println("Falsch beantwortet: " + stats.getWrongCount());
            System.out.printf("Erfolgsquote: %.2f%%\n", correctPercentage);
            System.out.println("Aktuelles Level: " + stats.getLevel());
            System.out.println("Fällig: " + (stats.isDue() ? "Ja" : "Nein"));
            if (stats.getLastCorrectAt() != null) {
                System.out.println("Zuletzt richtig: " + stats.getLastCorrectAt().format(DATE_TIME_FORMATTER));
            }
        } else {
            System.out.println("Status: Noch nie geübt");
            System.out.println("Level: 0");
            System.out.println("Fällig: Ja");
        }
        printSeparator();
    }

    public void showLastSessionResults() {
        displayResults(storage.loadSessionResults(), "Lernsessions");
    }

    public void showLastExamResults() {
        displayResults(storage.loadExamResults(), "Prüfungen");
    }

    private void displayResults(List<SessionResult> results, String type) {
        if (results.isEmpty()) {
            System.out.println("Noch keine " + type + " gespeichert.");
            return;
        }

        int limit = 3;
        int totalResults = results.size();
        int count = Math.min(limit, totalResults);

        printHeader("Ergebnisse der " + type + " (Top " + count + ")");

        for (int i = 0; i < count; i++) {
            // Zeige die neuesten Ergebnisse zuerst (von hinten in der Liste)
            SessionResult result = results.get(totalResults - 1 - i);
            printSessionResult(i + 1, result);
        }
    }

    private void printSessionResult(int index, SessionResult result) {
        System.out.printf("%d. %s (%s)\n", index, result.getSessionName(), result.getTimestamp().format(DATE_TIME_FORMATTER));
        System.out.printf("   Ergebnis: %d von %d richtig (%.2f%%)\n",
                result.getCorrectCount(), result.getTotalCount(), result.getPercentage());

        if (result.getTotalCount() > 0) {
            System.out.println("   Erreichte Note: " + FlashcardSessionStatistics.calculateGrade(result.getPercentage()));
        }
        if (result.getDurationMillis() > 0) {
            System.out.println("   Benötigte Zeit: " + FlashcardSessionStatistics.formatTime(result.getDurationMillis()));
        }
        System.out.println("------------------------------------------------");
    }

    private void printHeader(String title) {
        System.out.println("\n--- " + title + " ---");
    }

    private void printSeparator() {
        System.out.println("-----------------------");
    }

    private void printLevelDistribution(int[] levelDistribution) {
        System.out.println("\nLevel-Verteilung:");
        for (int i = 0; i <= 6; i++) {
            System.out.printf("  Level %d: %d Karten\n", i, levelDistribution[i]);
        }
    }
}
