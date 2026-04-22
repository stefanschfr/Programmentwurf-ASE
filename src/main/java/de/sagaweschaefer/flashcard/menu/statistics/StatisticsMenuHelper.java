package de.sagaweschaefer.flashcard.menu.statistics;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;
import de.sagaweschaefer.flashcard.model.SessionResult;
import de.sagaweschaefer.flashcard.util.FlashcardStorage;
import de.sagaweschaefer.flashcard.util.StatisticsCalculator;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StatisticsMenuHelper {
    private final FlashcardStorage storage;

    public StatisticsMenuHelper(FlashcardStorage storage) {
        this.storage = storage;
    }

    public void showGeneralStatistics() {
        List<FlashcardSet> sets = storage.loadFlashcardSets();
        Map<String, FlashcardStatistics> statsMap = storage.loadStatistics();

        StatisticsCalculator.GeneralStats stats = StatisticsCalculator.calculateGeneralStats(sets, statsMap);

        System.out.println("\n--- Gesamtstatistik ---");
        System.out.println("Gesamte Anzahl an Lernsets: " + stats.totalSets);
        System.out.println("Gesamte Anzahl an Fragen: " + stats.totalQuestions);
        System.out.println("Fragen richtig beantwortet: " + stats.totalCorrect);
        System.out.println("Fragen falsch beantwortet: " + stats.totalWrong);
        System.out.printf("Erfolgsquote: %.2f%%\n", stats.correctPercentage);
        System.out.println("-----------------------");
        System.out.println("Fällige Karten: " + stats.dueCards);
        System.out.println("Noch nie beantwortete Karten: " + stats.neverAnswered);
        System.out.printf("Durchschnittliches Level: %.2f\n", stats.averageLevel);
        
        System.out.println("\nLevel-Verteilung:");
        for (int i = 0; i <= 6; i++) {
            System.out.printf("  Level %d: %d Karten\n", i, stats.levelDistribution[i]);
        }
        
        if (!stats.mostFrequentQuestion.equals("N/A")) {
            System.out.println("\nMeistgeübte Karte: \"" + stats.mostFrequentQuestion + "\" (" + stats.mostFrequentCount + " Versuche)");
        }
    }
    public void showSetStatistics() {
        List<FlashcardSet> sets = storage.loadFlashcardSets();
        if (sets.isEmpty()) {
            System.out.println("Keine Lernsets vorhanden.");
            return;
        }

        de.sagaweschaefer.flashcard.util.MenuUtils.displayFlashcardSets(sets, "Statistik nach Set");
        FlashcardSet selectedSet = de.sagaweschaefer.flashcard.util.MenuUtils.selectFromList(sets, "Bitte wählen Sie ein Set aus (0 zum Abbrechen): ");

        if (selectedSet != null) {
            Map<String, FlashcardStatistics> statsMap = storage.loadStatistics();

            int totalQuestions = selectedSet.getFlashcardSet().size();
            int totalCorrect = 0;
            int totalWrong = 0;
            int dueCards = 0;
            int[] levelDistribution = new int[7];

            for (de.sagaweschaefer.flashcard.model.Flashcard card : selectedSet.getFlashcardSet()) {
                FlashcardStatistics stats = statsMap.get(card.getId());
                if (stats != null) {
                    totalCorrect += stats.getCorrectCount();
                    totalWrong += stats.getWrongCount();
                    if (stats.isDue()) dueCards++;
                    int level = stats.getLevel();
                    if (level >= 0 && level <= 6) levelDistribution[level]++;
                } else {
                    dueCards++;
                    levelDistribution[0]++;
                }
            }

            int totalAnswers = totalCorrect + totalWrong;
            double correctPercentage = totalAnswers > 0 ? (double) totalCorrect / totalAnswers * 100 : 0;

            System.out.println("\n--- Statistik für Set: " + selectedSet.getName() + " ---");
            System.out.println("Anzahl Fragen: " + totalQuestions);
            System.out.println("Richtig beantwortet: " + totalCorrect);
            System.out.println("Falsch beantwortet: " + totalWrong);
            System.out.printf("Erfolgsquote: %.2f%%\n", correctPercentage);
            System.out.println("Fällige Karten: " + dueCards);
            
            System.out.println("\nLevel-Verteilung:");
            for (int i = 0; i <= 6; i++) {
                System.out.printf("  Level %d: %d Karten\n", i, levelDistribution[i]);
            }
        }
    }

    public void showCardStatistics() {
        List<FlashcardSet> sets = storage.loadFlashcardSets();
        if (sets.isEmpty()) {
            System.out.println("Keine Lernsets vorhanden.");
            return;
        }

        de.sagaweschaefer.flashcard.util.MenuUtils.displayFlashcardSets(sets, "Einzelspezifische Statistik: Set wählen");
        FlashcardSet selectedSet = de.sagaweschaefer.flashcard.util.MenuUtils.selectFromList(sets, "Bitte wählen Sie ein Set aus (0 zum Abbrechen): ");

        if (selectedSet != null) {
            List<de.sagaweschaefer.flashcard.model.Flashcard> cards = selectedSet.getFlashcardSet();
            
            if (cards.isEmpty()) {
                System.out.println("Dieses Set enthält keine Karten.");
                return;
            }

            Map<String, FlashcardStatistics> statsMap = storage.loadStatistics();

            System.out.println("\n--- Karten im Set: " + selectedSet.getName() + " ---");
            for (int i = 0; i < cards.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, cards.get(i).getQuestion());
            }

            de.sagaweschaefer.flashcard.model.Flashcard selectedCard = de.sagaweschaefer.flashcard.util.MenuUtils.selectFromList(cards, "Bitte wählen Sie eine Karte aus (0 zum Abbrechen): ");

            if (selectedCard != null) {
                FlashcardStatistics stats = statsMap.get(selectedCard.getId());

                System.out.println("\n--- Statistik für Frage ---");
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
                        System.out.println("Zuletzt richtig: " + stats.getLastCorrectAt().format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
                    }
                } else {
                    System.out.println("Status: Noch nie geübt");
                    System.out.println("Level: 0");
                    System.out.println("Fällig: Ja");
                }
                System.out.println("---------------------------");
            }
        }
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

        Collections.reverse(results);
        int pageSize = 3;
        int totalResults = results.size();
        int totalPages = (int) Math.ceil((double) totalResults / pageSize);
        int currentPage = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        while (true) {
            System.out.println("\n--- Ergebnisse der " + type + " (Seite " + (currentPage + 1) + " von " + totalPages + ") ---");
            
            int start = currentPage * pageSize;
            int end = Math.min(start + pageSize, totalResults);

            for (int i = start; i < end; i++) {
                SessionResult result = results.get(i);
                System.out.printf("%d. %s (%s)\n", i + 1, result.getSessionName(), result.getTimestamp().format(formatter));
                System.out.printf("   Ergebnis: %d von %d richtig (%.2f%%)\n",
                    result.getCorrectCount(), result.getTotalCount(), result.getPercentage());
                if (result.getTotalCount() > 0) {
                    System.out.println("   Erreichte Note: " + de.sagaweschaefer.flashcard.menu.flashcardsession.FlashcardSessionStatistics.calculateGrade(result.getPercentage()));
                }
                if (result.getDurationMillis() > 0) {
                    System.out.println("   Benötigte Zeit: " + de.sagaweschaefer.flashcard.menu.flashcardsession.FlashcardSessionStatistics.formatTime(result.getDurationMillis()));
                }
                System.out.println("------------------------------------------------");
            }

            System.out.println("\nOptionen:");
            if (currentPage < totalPages - 1) {
                System.out.println("(N)ächste Seite");
            }
            if (currentPage > 0) {
                System.out.println("(V)orherige Seite");
            }
            System.out.println("(B)eenden");

            String choice = de.sagaweschaefer.flashcard.util.MenuUtils.promptForString("Wählen Sie eine Option: ").toUpperCase();

            if (choice.equals("N") && currentPage < totalPages - 1) {
                currentPage++;
            } else if (choice.equals("V") && currentPage > 0) {
                currentPage--;
            } else if (choice.equals("B")) {
                break;
            } else {
                System.out.println("Ungültige Auswahl.");
            }
        }
        
        // Liste wieder in den Originalzustand bringen, falls sie woanders noch verwendet wird (obwohl hier lokal)
        Collections.reverse(results);
    }
}
