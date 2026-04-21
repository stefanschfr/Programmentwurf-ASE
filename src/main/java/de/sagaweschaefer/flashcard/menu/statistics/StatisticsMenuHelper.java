package de.sagaweschaefer.flashcard.menu.statistics;
import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;
import de.sagaweschaefer.flashcard.util.JsonStorage;

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

        for (FlashcardSet set : sets) {
            totalQuestions += set.getFlashcardSet().size();
        }

        for (FlashcardStatistics stats : statsMap.values()) {
            totalCorrect += stats.getCorrectCount();
            totalWrong += stats.getWrongCount();
        }

        int totalAnswers = totalCorrect + totalWrong;
        double correctPercentage = totalAnswers > 0 ? (double) totalCorrect / totalAnswers * 100 : 0;

        System.out.println("\n--- Gesamtstatistik ---");
        System.out.println("Gesamte Anzahl an Lernsets: " + totalSets);
        System.out.println("Gesamte Anzahl an Fragen: " + totalQuestions);
        System.out.println("Fragen richtig beantwortet: " + totalCorrect);
        System.out.println("Fragen falsch beantwortet: " + totalWrong);
        System.out.printf("Erfolgsquote: %.2f%%\n", correctPercentage);
    }
    public void showSetStatistics() {
        System.out.println("\n--- Statistik nach Set ---");
    }
}
