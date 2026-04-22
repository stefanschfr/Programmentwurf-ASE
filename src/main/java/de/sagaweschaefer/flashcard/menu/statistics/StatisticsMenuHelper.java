package de.sagaweschaefer.flashcard.menu.statistics;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;
import de.sagaweschaefer.flashcard.model.SessionResult;
import de.sagaweschaefer.flashcard.util.JsonStorage;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StatisticsMenuHelper {
    private final JsonStorage jsonStorage = new JsonStorage();

    public void showGeneralStatistics() {
        List<FlashcardSet> sets = jsonStorage.loadFlashcardSets();
        Map<String, FlashcardStatistics> statsMap = jsonStorage.loadStatistics();

        int totalSets = sets.size();
        int totalQuestions = 0;
        int totalCorrect = 0;
        int totalWrong = 0;
        int neverAnswered = 0;
        int dueCards = 0;
        int[] levelDistribution = new int[7];
        double totalLevel = 0;

        FlashcardStatistics mostFrequentStats = null;
        String mostFrequentQuestion = "N/A";

        Map<String, String> idToQuestionMap = new java.util.HashMap<>();
        for (FlashcardSet set : sets) {
            for (de.sagaweschaefer.flashcard.model.Flashcard card : set.getFlashcardSet()) {
                totalQuestions++;
                idToQuestionMap.put(card.getId(), card.getQuestion());
            }
        }

        for (FlashcardStatistics stats : statsMap.values()) {
            totalCorrect += stats.getCorrectCount();
            totalWrong += stats.getWrongCount();
            
            if (stats.getCorrectCount() == 0 && stats.getWrongCount() == 0) {
                neverAnswered++;
            }
            
            if (stats.isDue()) {
                dueCards++;
            }
            
            int level = stats.getLevel();
            if (level >= 0 && level <= 6) {
                levelDistribution[level]++;
            }
            totalLevel += level;

            if (mostFrequentStats == null || 
                (stats.getCorrectCount() + stats.getWrongCount()) > (mostFrequentStats.getCorrectCount() + mostFrequentStats.getWrongCount())) {
                mostFrequentStats = stats;
            }
        }
        
        // Karten ohne Statistiken zählen auch als "noch nie beantwortet"
        int totalCardsInStats = statsMap.keySet().size();
        int missingCards = totalQuestions - totalCardsInStats;
        neverAnswered += Math.max(0, missingCards);
        
        dueCards += Math.max(0, missingCards);
        levelDistribution[0] += Math.max(0, missingCards);

        if (mostFrequentStats != null && (mostFrequentStats.getCorrectCount() + mostFrequentStats.getWrongCount()) > 0) {
            mostFrequentQuestion = idToQuestionMap.getOrDefault(mostFrequentStats.getFlashcardId(), "Unbekannte Frage");
        }

        int totalAnswers = totalCorrect + totalWrong;
        double correctPercentage = totalAnswers > 0 ? (double) totalCorrect / totalAnswers * 100 : 0;
        double averageLevel = totalQuestions > 0 ? totalLevel / totalQuestions : 0;

        System.out.println("\n--- Gesamtstatistik ---");
        System.out.println("Gesamte Anzahl an Lernsets: " + totalSets);
        System.out.println("Gesamte Anzahl an Fragen: " + totalQuestions);
        System.out.println("Fragen richtig beantwortet: " + totalCorrect);
        System.out.println("Fragen falsch beantwortet: " + totalWrong);
        System.out.printf("Erfolgsquote: %.2f%%\n", correctPercentage);
        System.out.println("-----------------------");
        System.out.println("Fällige Karten: " + dueCards);
        System.out.println("Noch nie beantwortete Karten: " + neverAnswered);
        System.out.printf("Durchschnittliches Level: %.2f\n", averageLevel);
        
        System.out.println("\nLevel-Verteilung:");
        for (int i = 0; i <= 6; i++) {
            System.out.printf("  Level %d: %d Karten\n", i, levelDistribution[i]);
        }
        
        if (!mostFrequentQuestion.equals("N/A")) {
            int count = mostFrequentStats.getCorrectCount() + mostFrequentStats.getWrongCount();
            System.out.println("\nMeistgeübte Karte: \"" + mostFrequentQuestion + "\" (" + count + " Versuche)");
        }
    }
    public void showSetStatistics() {
        List<FlashcardSet> sets = jsonStorage.loadFlashcardSets();
        if (sets.isEmpty()) {
            System.out.println("Keine Lernsets vorhanden.");
            return;
        }

        de.sagaweschaefer.flashcard.util.MenuUtils.displayFlashcardSets(sets, "Statistik nach Set");
        int selection = de.sagaweschaefer.flashcard.util.MenuUtils.promptForInt("Bitte wählen Sie ein Set aus (0 zum Abbrechen): ");

        if (selection > 0 && selection <= sets.size()) {
            FlashcardSet selectedSet = sets.get(selection - 1);
            Map<String, FlashcardStatistics> statsMap = jsonStorage.loadStatistics();

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
        List<FlashcardSet> sets = jsonStorage.loadFlashcardSets();
        if (sets.isEmpty()) {
            System.out.println("Keine Lernsets vorhanden.");
            return;
        }

        de.sagaweschaefer.flashcard.util.MenuUtils.displayFlashcardSets(sets, "Einzelspezifische Statistik: Set wählen");
        int setSelection = de.sagaweschaefer.flashcard.util.MenuUtils.promptForInt("Bitte wählen Sie ein Set aus (0 zum Abbrechen): ");

        if (setSelection > 0 && setSelection <= sets.size()) {
            FlashcardSet selectedSet = sets.get(setSelection - 1);
            List<de.sagaweschaefer.flashcard.model.Flashcard> cards = selectedSet.getFlashcardSet();
            
            if (cards.isEmpty()) {
                System.out.println("Dieses Set enthält keine Karten.");
                return;
            }

            System.out.println("\n--- Karten im Set: " + selectedSet.getName() + " ---");
            for (int i = 0; i < cards.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, cards.get(i).getQuestion());
            }

            int cardSelection = de.sagaweschaefer.flashcard.util.MenuUtils.promptForInt("Bitte wählen Sie eine Karte aus (0 zum Abbrechen): ");

            if (cardSelection > 0 && cardSelection <= cards.size()) {
                de.sagaweschaefer.flashcard.model.Flashcard selectedCard = cards.get(cardSelection - 1);
                Map<String, FlashcardStatistics> statsMap = jsonStorage.loadStatistics();
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
        List<SessionResult> results = jsonStorage.loadSessionResults();
        if (results.isEmpty()) {
            System.out.println("Noch keine Lernsessions gespeichert.");
            return;
        }

        System.out.println("\n--- Ergebnisse der letzten drei Lernsessions ---");
        Collections.reverse(results);
        int count = Math.min(3, results.size());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        for (int i = 0; i < count; i++) {
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
    }
}
